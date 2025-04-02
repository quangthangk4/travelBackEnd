package com.travel.travelling.service;

import com.travel.travelling.constant.TicketStatus;
import com.travel.travelling.dto.request.TicketBookRequest;
import com.travel.travelling.dto.response.TicketBookedResponse;
import com.travel.travelling.dto.response.TicketResponse;
import com.travel.travelling.entity.Flight;
import com.travel.travelling.entity.Ticket;
import com.travel.travelling.entity.TicketId;
import com.travel.travelling.entity.User;
import com.travel.travelling.exception.AppException;
import com.travel.travelling.exception.ErrorCode;
import com.travel.travelling.mapper.TicketMapper;
import com.travel.travelling.repository.FlightRepository;
import com.travel.travelling.repository.TicketRepository;
import com.travel.travelling.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {


    private final FlightRepository flightRepository;
    private final UserService userService;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final UserRepository userRepository;

    // create ticket when create flight
    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public TicketResponse bookTicket(TicketBookRequest request, double seatPrice){
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED));

        // 2️⃣ Kiểm tra người dùng có tồn tại không
        User user = userRepository.findByEmail(userService.getMyInfo().getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        Optional<Ticket> ticketCheck = ticketRepository.findByUserAndFlight(user, flight);

        if (ticketCheck.isPresent())
            throw new AppException(ErrorCode.SEAT_ALREADY_BOOK_BY_YOU);

        // kiểm tra xem ghế chuẩn bị đặt có hợp lệ không
        if(!isValidSeatNumber(request.getSeatNumber(), flight.getTotalTickets()))
            throw new AppException(ErrorCode.SEAT_NUMBER_INVALID);

        // 3️⃣ Kiểm tra ghế có bị đặt chưa
        boolean seatBooked = ticketRepository.existsByFlightAndSeatNumber(flight, request.getSeatNumber());
        if (seatBooked) {
            throw new AppException(ErrorCode.SEAT_ALREADY_BOOKED);
        }

        // trừ tiền vé máy bay
        if(user.getAccountBalance() < seatPrice) throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        user.setAccountBalance(user.getAccountBalance() - seatPrice);
        userRepository.save(user);

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
        // 1️ Kiểm tra định dạng mới (VD: 12A, 23B)
        if (!seatNumber.matches("^[0-9]+[A-F]$")) {
            return false;
        }

        // 2️ Lấy số ghế và hàng ghế từ chuỗi
        int seatNum = Integer.parseInt(seatNumber.substring(0, seatNumber.length() - 1)); // Lấy số ghế

        // 3️ Tính tổng số ghế trên mỗi hàng
        int maxSeatsPerRow = totalSeats / 6;

        // 4️ Kiểm tra số ghế có hợp lệ không
        return seatNum >= 1 && seatNum <= maxSeatsPerRow;
    }

    // lấy tất cả ghế đã được đặt,
    public List<String> ticketBookedResponse(String flightId){
        List<Ticket> tickets = ticketRepository.findAllByFlightIdAndAvailable(flightId, false);
        return tickets.stream().map(Ticket::getSeatNumber).collect(Collectors.toList());
    }


    // kiểm tra ghế có gần cửa sổ không
    private boolean isWindowSeat(String seatNumber) {
        if (seatNumber == null || seatNumber.length() < 2) return false;

        char row = seatNumber.charAt(seatNumber.length() - 1); // Lấy chữ cái cuối cùng (A, B, C, D, E, F)
        return row == 'A' || row == 'F'; // Ghế 'A' và 'F' là ghế cửa sổ
    }



    // get my ticket (chưa bay)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<TicketResponse> getMyTickets(){
        User user = userRepository.findByEmail(userService.getMyInfo().getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        List<Ticket> tickets = ticketRepository.findByUser(user);
        return tickets.stream()
                .map(ticketMapper::toTicketResponse)
                .collect(Collectors.toList());
    }


    public boolean isTicketOwner(String flightId, String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return false;

        TicketId ticketId = new TicketId(user.getId(), flightId);
        return ticketRepository.existsById(ticketId);
    }


    // get ticket when user click one
    @PreAuthorize("@ticketService.isTicketOwner(#flightId, authentication.name)")
    public TicketResponse getTicketById(String flightId){
        User user = userRepository.findByEmail(userService.getMyInfo().getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        TicketId ticketId = new TicketId(user.getId(), flightId);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new AppException(ErrorCode.TICKET_NOT_EXISTED)
        );

        return ticketMapper.toTicketResponse(ticket);
    }

    @Transactional
    @PreAuthorize("@ticketService.isTicketOwner(#flightId, authentication.name)")
    public String cancelTicket(String flightId){
        User user = userRepository.findByEmail(userService.getMyInfo().getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        TicketId ticketId = new TicketId(user.getId(), flightId);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new AppException(ErrorCode.TICKET_NOT_EXISTED)
        );

        // hủy vé
        ticketRepository.delete(ticket);


        // hoàn tiền
        double priceReturn =  ticket.getPrice()/2;
        user.setAccountBalance(user.getAccountBalance() + priceReturn);
        String message = String.format("hủy vé thành công, đã hoàn trả lại %.2f đồng vào tài khoản", priceReturn);
        userRepository.save(user);

        return message;
    }
}
