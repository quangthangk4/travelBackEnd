package com.travel.travelling.service;

import com.travel.travelling.dto.request.ConfirmPaymentRequest;
import com.travel.travelling.dto.request.TicketBookRequest;
import com.travel.travelling.entity.Flight;
import com.travel.travelling.exception.AppException;
import com.travel.travelling.exception.ErrorCode;
import com.travel.travelling.repository.FlightRepository;
import com.travel.travelling.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketHoldService {

    private static final String SEAT_HOLD_KEY = "seat-hold:";
    private static final long HOLD_TIME_SECONDS = 300; // 5 phút giữ ghế

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;
    private final FlightRepository flightRepository;
    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
//    private final SimpMessagingTemplate messagingTemplate;

    // Hàm tạo seatKey
    private String createSeatKey(Flight flight, String seatNumber) {
        return "seat:" + flight.getFlightNumber() + ":" + seatNumber;
    }

    // Hàm kiểm tra chuyến bay tồn tại
    private Flight getFlightOrThrow(String flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED));
    }

    /** Giữ ghế */
    public boolean holdSeat(TicketBookRequest request) {
        Flight flight = getFlightOrThrow(request.getFlightId());
        String seatKey = createSeatKey(flight, request.getSeatNumber());
        String userId = userService.getMyInfo().getId();

        // Lua script đảm bảo giữ ghế an toàn
        String luaScript = "if redis.call('EXISTS', KEYS[1]) == 0 then " +
                "redis.call('HSET', KEYS[1], 'user', ARGV[1], 'status', 'HOLD'); " +
                "redis.call('EXPIRE', KEYS[1], ARGV[2]); " +
                "return 1; " +
                "elseif redis.call('HGET', KEYS[1], 'user') == ARGV[1] then " +
                "redis.call('EXPIRE', KEYS[1], ARGV[2]); " +  // Gia hạn giữ ghế nếu là cùng 1 user
                "return 1; " +
                "else return 0; end";


        boolean result = redisTemplate.execute(
                new DefaultRedisScript<>(luaScript, Boolean.class),
                Collections.singletonList(seatKey),
                userId, HOLD_TIME_SECONDS
        );

        if (!result) throw new AppException(ErrorCode.SEAT_ALREADY_HELD_BY_SOMEONE);

        return result;
    }

    /** Gia hạn giữ ghế */
    public void extendSeatHold(TicketBookRequest request) {
        Flight flight = getFlightOrThrow(request.getFlightId());
        String seatKey = createSeatKey(flight, request.getSeatNumber());
        String userId = userService.getMyInfo().getId();

        if (isSeatHeldByUser(seatKey, userId)) {
            redisTemplate.expire(seatKey, HOLD_TIME_SECONDS, TimeUnit.SECONDS);
        } else {
            throw new AppException(ErrorCode.SEAT_NOT_HELD_BY_USER);
        }
    }

    /** Xóa giữ ghế */
    public void deleteHoldSeat(TicketBookRequest request) {
        Flight flight = getFlightOrThrow(request.getFlightId());
        String seatKey = createSeatKey(flight, request.getSeatNumber());
        String userId = userService.getMyInfo().getId();

        if (isSeatHeldByUser(seatKey, userId)) {
            redisTemplate.delete(seatKey);
        } else {
            throw new AppException(ErrorCode.SEAT_NOT_HELD_BY_USER);
        }
    }

    /** Kiểm tra ghế có phải của người dùng không */
    private boolean isSeatHeldByUser(String seatKey, String userId) {
        Object currentUser = redisTemplate.opsForHash().get(seatKey, "user");
        return userId.equals(currentUser);
    }

    /**
     * Xác nhận thanh toán
     */
    @Transactional
    public void confirmPayment(ConfirmPaymentRequest request) {
        TicketBookRequest ticketBookRequestArrival = TicketBookRequest.builder()
                .flightId(request.getFlightIdArrival())
                .seatNumber(request.getSeatNumberArrival())
                .build();

        // thanh toán chuyến bay đi
        checkConfirmation(ticketBookRequestArrival, request.getTotalPriceArrival());

        // k phải vé khứ hồi thì return
        if(!request.getIsRoundTrip()) return;

        TicketBookRequest ticketBookRequestReturn = TicketBookRequest.builder()
                .flightId(request.getFlightIdReturn())
                .seatNumber(request.getSeatNumberReturn())
                .build();

        checkConfirmation(ticketBookRequestReturn, request.getTotalPriceReturn());
    }


    public void checkConfirmation(TicketBookRequest request, double seatPrice) {
        Flight flight = getFlightOrThrow(request.getFlightId());
        String seatKey = createSeatKey(flight, request.getSeatNumber());
        String userId = userService.getMyInfo().getId();

        // Lua script kiểm tra & xóa cùng lúc để đảm bảo không có race condition
        String luaScript = "if redis.call('HGET', KEYS[1], 'user') == ARGV[1] then " +
                "redis.call('DEL', KEYS[1]); return 1; else return 0; end";

        if (ticketRepository.existsByFlightAndSeatNumber(flight, request.getSeatNumber())) {
            throw new AppException(ErrorCode.SEAT_ALREADY_BOOKED);
        }

        boolean success = redisTemplate.execute(
                new DefaultRedisScript<>(luaScript, Boolean.class),
                Collections.singletonList(seatKey), userId
        );


        if (!success) {
            throw new AppException(ErrorCode.SEAT_NOT_HELD_BY_USER);
        }

        ticketService.bookTicket(request, seatPrice);
    }


    public Set<String> getAllHeldSeats(String flightId) {
        Flight flight = getFlightOrThrow(flightId);
        String ListSeatName = String.format("seat:%s:*", flight.getFlightNumber());

        log.info("ListSeatName: {}", ListSeatName);
        Set<String> seatKeys = redisTemplate.keys(ListSeatName); // Lấy tất cả các ghế đang giữ
        Set<String> heldSeats = new HashSet<>();

        for (String seatKey : seatKeys) {
            String[] parts = seatKey.split(":");
            if (parts.length == 3) {
                heldSeats.add(parts[2]); // Chỉ lấy số ghế
            }
        }

        log.info("heldSeats: {}", heldSeats);


        return heldSeats; // Trả về danh sách ghế đang giữ và người giữ
    }

    @Scheduled(fixedRate = 60000) // Mỗi 1 phút
    public void releaseExpiredSeats() {
        Set<String> seatKeys = redisTemplate.keys("seat:*");

        long now = System.currentTimeMillis();
        for (String seatKey : seatKeys) {
            Object expireTime = redisTemplate.opsForHash().get(seatKey, "expire");
            if (expireTime == null) continue;

            try {
                long expiry = Long.parseLong(expireTime.toString());
                if (expiry < now) {
                    redisTemplate.delete(seatKey);
                    System.out.println("Released seat: " + seatKey);
                }
            } catch (NumberFormatException e) {
                redisTemplate.delete(seatKey); // Xóa nếu expire bị lỗi
            }
        }
    }
}
