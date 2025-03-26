package com.travel.travelling.service;

import com.travel.travelling.dto.request.TicketBookRequest;
import com.travel.travelling.dto.response.TicketResponse;
import com.travel.travelling.entity.Flight;
import com.travel.travelling.exception.AppException;
import com.travel.travelling.exception.ErrorCode;
import com.travel.travelling.repository.FlightRepository;
import com.travel.travelling.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

    /** Giữ ghế */
    public boolean holdSeat(TicketBookRequest request) {
        String userId = userService.getMyInfo().getId();
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED));

        String seatKey = "seat:" + flight.getFlightNumber() + ":" + request.getSeatNumber();

        // Lua script đảm bảo giữ ghế an toàn
        String luaScript = "if redis.call('EXISTS', KEYS[1]) == 0 then " +
                "redis.call('HSET', KEYS[1], 'user', ARGV[1], 'status', 'HOLD'); " +
                "redis.call('EXPIRE', KEYS[1], ARGV[2]); " +
                "return 1; " +
                "else return 0; end";

        boolean success = redisTemplate.execute(
                new DefaultRedisScript<>(luaScript, Boolean.class),
                Collections.singletonList(seatKey),
                userId, HOLD_TIME_SECONDS
        );

        return success;
    }




    public void extendSeatHold(TicketBookRequest request) {
        String userId = userService.getMyInfo().getId();
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED));

        String seatKey = "seat:" + flight.getFlightNumber() + ":" + request.getSeatNumber();

        if (isSeatHeldByUser(seatKey, userId)) {
            redisTemplate.expire(seatKey, HOLD_TIME_SECONDS, TimeUnit.SECONDS);
        }
        else {
            throw new AppException(ErrorCode.SEAT_NOT_HELD_BY_USER);
        }
    }



    public void deleteHoldSeat(TicketBookRequest request) {
        String userId = userService.getMyInfo().getId();
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED));

        String seatKey = "seat:" + flight.getFlightNumber() + ":" + request.getSeatNumber();

        if (isSeatHeldByUser(seatKey, userId)) {
            redisTemplate.delete(seatKey);
        }
        else {
            throw new AppException(ErrorCode.SEAT_NOT_HELD_BY_USER);
        }
    }




    private boolean isSeatHeldByUser(String seatKey, String userId) {
        Object currentUser = redisTemplate.opsForHash().get(seatKey, "user");
        return userId.equals(currentUser);
    }


    @Transactional
    public TicketResponse confirmPayment(TicketBookRequest request) {
        String userId = userService.getMyInfo().getId();
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED));

        String seatKey = "seat:" + flight.getFlightNumber() + ":" + request.getSeatNumber();

        // Lua script kiểm tra & xóa cùng lúc để đảm bảo không có race condition
        String luaScript = "if redis.call('HGET', KEYS[1], 'user') == ARGV[1] then " +
                "redis.call('DEL', KEYS[1]); return 1; else return 0; end";

        boolean seatBooked = ticketRepository.existsByFlightAndSeatNumber(flight, request.getSeatNumber());
        if (seatBooked) {
            throw new AppException(ErrorCode.SEAT_ALREADY_BOOKED);
        }

        boolean success = redisTemplate.execute(
                new DefaultRedisScript<>(luaScript, Boolean.class),
                Collections.singletonList(seatKey), userId);

        if (!success) {
            throw new AppException(ErrorCode.SEAT_NOT_HELD_BY_USER);
        }

        // Lưu vào database
        return ticketService.bookTicket(request);
    }


    @Scheduled(fixedRate = 60000) // Mỗi 1 phút
    public void releaseExpiredSeats() {
        Set<String> seatKeys = redisTemplate.keys("seat:*");

        long now = System.currentTimeMillis();
        for (String seatKey : seatKeys) {
            Object expireTime = redisTemplate.opsForHash().get(seatKey, "expire");
            if (expireTime == null) continue;  // Tránh NullPointerException

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
