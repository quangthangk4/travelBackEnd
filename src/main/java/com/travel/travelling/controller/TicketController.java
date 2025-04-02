package com.travel.travelling.controller;

import com.travel.travelling.dto.request.ConfirmPaymentRequest;
import com.travel.travelling.dto.request.TicketBookRequest;
import com.travel.travelling.dto.response.ApiResponse;
import com.travel.travelling.dto.response.TicketResponse;
import com.travel.travelling.service.TicketHoldService;
import com.travel.travelling.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {


    private final TicketService ticketService;
    private final TicketHoldService ticketHoldService;

    @PostMapping("/book")
    public ApiResponse<Void> confirmPayment(@RequestBody ConfirmPaymentRequest request){
        log.info("confirm payment {}", request.toString());
        ticketHoldService.confirmPayment(request);
        return ApiResponse.<Void>builder()
                .message("book ticket successfully")
                .build();
    }

    @PostMapping("/hold-seat")
    public ApiResponse<Boolean> holdSeat(@RequestBody TicketBookRequest request){
        boolean holdSeat = ticketHoldService.holdSeat(request);
        String message = "hold seat successfully";
        if (!holdSeat) message = "ghế đang có người giữ";
        return ApiResponse.<Boolean>builder()
                .message(message)
                .result(holdSeat)
                .build();
    }

    @GetMapping("/booked-seats/{flightId}")
    public ApiResponse<List<String>> getAvailableTickets(@PathVariable String flightId){
        return ApiResponse.<List<String>>builder()
                .result(ticketService.ticketBookedResponse(flightId))
                .build();
    }


    @PostMapping("/delete-hold-seat")
    public ApiResponse<Void> deleteHoldSeat(@RequestBody TicketBookRequest request){
        ticketHoldService.deleteHoldSeat(request);
        return ApiResponse.<Void>builder()
                .message("delete hold seat successfully")
                .build();
    }

    @PutMapping("/extend-hold-seat")
    public ApiResponse<TicketResponse> extendHoldSeat(@RequestBody TicketBookRequest request){
        ticketHoldService.extendSeatHold(request);
        return ApiResponse.<TicketResponse>builder()
                .message("extend hold seat successfully")
                .build();
    }



    @GetMapping("/getMyTickets")
    public ApiResponse<List<TicketResponse>> getUpcomingTickets(){
        return ApiResponse.<List<TicketResponse>>builder()
                .message("get upcoming tickets successfully")
                .result(ticketService.getMyTickets())
                .build();
    }


    @GetMapping("/{flightId}")
    public ApiResponse<TicketResponse> getTicket(@PathVariable String flightId){
        return ApiResponse.<TicketResponse>builder()
                .message("get ticket successfully")
                .result(ticketService.getTicketById(flightId))
                .build();
    }

    @DeleteMapping("/cancel-ticket/{flightId}")
    public ApiResponse<String> cancelTicket(@PathVariable String flightId){
        return ApiResponse.<String>builder()
                .result(ticketService.cancelTicket(flightId))
                .build();
    }

    @GetMapping("/getAllHeldSeats/{flightId}")
    public ApiResponse<Set<String>> getAllHeldSeats(@PathVariable String flightId){
        return ApiResponse.<Set<String>>builder()
                .message("get all held seats successfully")
                .result(ticketHoldService.getAllHeldSeats(flightId))
                .build();
    }

}
