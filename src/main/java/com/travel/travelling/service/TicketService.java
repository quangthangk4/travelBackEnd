package com.travel.travelling.service;

import com.travel.travelling.constant.TicketStatus;
import com.travel.travelling.dto.request.TicketBookRequest;
import com.travel.travelling.dto.response.TicketResponse;
import com.travel.travelling.entity.Flight;
import com.travel.travelling.entity.Ticket;
import com.travel.travelling.entity.TicketId;
import com.travel.travelling.entity.User;
import com.travel.travelling.exception.AppException;
import com.travel.travelling.exception.ErrorCode;
import com.travel.travelling.mapper.TicketMapper;
import com.travel.travelling.mapper.UserMapper;
import com.travel.travelling.repository.FlightRepository;
import com.travel.travelling.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketService {


    private final FlightRepository flightRepository;
    private final UserService userService;
    private final TicketRepository ticketRepository;
    private final UserMapper userMapper;
    private final TicketMapper ticketMapper;

    // create ticket when create flight
    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public TicketResponse createTickets(TicketBookRequest request){
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED));

        // 2️⃣ Kiểm tra người dùng có tồn tại không
        User user = userMapper.toUser(userService.getMyInfo());

        // kiểm tra xem ghế chuẩn bị đặt có hợp lệ không
        if(!isValidSeatNumber(request.getSeatNumber(), flight.getTotalTickets()))
            throw new AppException(ErrorCode.SEAT_NUMBER_INVALID);

        // 3️⃣ Kiểm tra ghế có bị đặt chưa
        boolean seatBooked = ticketRepository.existsByFlightAndSeatNumber(flight, request.getSeatNumber());
        if (seatBooked) {
            throw new AppException(ErrorCode.SEAT_ALREADY_BOOKED);
        }


        // 4️⃣ Xác định giá vé dựa trên vị trí ghế
        double basePrice = 100_000.0;
        double seatPrice = basePrice + (isWindowSeat(request.getSeatNumber()) ? 50_000.0 : 0.0);

        // 5️⃣ Lưu vé vào database
        Ticket ticket = Ticket.builder()
                .id(new TicketId(user.getId(), flight.getId()))
                .user(user)
                .flight(flight)
                .seatNumber(request.getSeatNumber())
                .bookingDate(LocalDateTime.now())
                .status(TicketStatus.BOOKED)
                .price(seatPrice)
                .available(false) // Ghế đã được đặt
                .build();

        try {
            ticket = ticketRepository.save(ticket);
            flight.setSoldTickets(flight.getSoldTickets() + 1);
            flightRepository.save(flight);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu Ticket: " + e.getMessage());
        }

        // 6️⃣ Trả về response
        return ticketMapper.toTicketResponse(ticket);
    }

    // check ghế có hợp lệ không
    public boolean isValidSeatNumber(String seatNumber, int totalSeats) {
        // 1️⃣ Kiểm tra định dạng ghế (VD: A1, B10)
        if (!seatNumber.matches("^[A-F][0-9]+$")) {
            return false;
        }

        // 2️⃣ Lấy hàng ghế và số ghế từ chuỗi
        char row = seatNumber.charAt(0); // Chữ cái đầu (hàng ghế)
        int seatNum = Integer.parseInt(seatNumber.substring(1)); // Số ghế

        // 3️⃣ Tính tổng số ghế trên mỗi hàng
        int maxSeatsPerRow = totalSeats / 6;

        // 4️⃣ Kiểm tra số ghế có hợp lệ không
        return seatNum >= 1 && seatNum <= maxSeatsPerRow;
    }


    // kiểm tra ghế có gần cửa sổ không
    private boolean isWindowSeat(String seatNumber) {
        if (seatNumber == null || seatNumber.length() < 2) return false;

        char row = seatNumber.charAt(0); // Lấy ký tự đầu tiên, ví dụ: "A1" -> 'A'
        return row == 'A' || row == 'F'; // Ghế 'A' và 'F' là ghế cửa sổ
    }
}
