package com.travel.travelling.controller;

import com.travel.travelling.dto.request.TicketBookRequest;
import com.travel.travelling.dto.response.ApiResponse;
import com.travel.travelling.dto.response.TicketBookedResponse;
import com.travel.travelling.dto.response.TicketResponse;
import com.travel.travelling.service.TicketHoldService;
import com.travel.travelling.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {


    private final TicketService ticketService;
    private final TicketHoldService ticketHoldService;

    @PostMapping("/book")
    public ApiResponse<TicketResponse> confirmPayment(@RequestBody TicketBookRequest request){
        return ApiResponse.<TicketResponse>builder()
                .message("book ticket successfully")
                .result(ticketHoldService.confirmPayment(request))
                .build();
    }

    @PostMapping("/hold-seat")
    public ApiResponse<Void> holdSeat(@RequestBody TicketBookRequest request){
        boolean holdSeat = ticketHoldService.holdSeat(request);
        String message = "hold seat successfully";
        if (!holdSeat) message = "ghế đang có người giữ";
        return ApiResponse.<Void>builder()
                .message(message)
                .build();
    }

    @GetMapping("/available/{flightId}")
    public ApiResponse<List<TicketBookedResponse>> getAvailableTickets(@PathVariable String flightId){
        return ApiResponse.<List<TicketBookedResponse>>builder()
                .result(ticketService.ticketBookedResponse(flightId))
                .build();
    }


    @DeleteMapping("/delete-hold-seat")
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



    @GetMapping("/upcoming")
    public ApiResponse<List<TicketResponse>> getUpcomingTickets(){
        return ApiResponse.<List<TicketResponse>>builder()
                .message("get upcoming tickets successfully")
                .result(ticketService.getUpComingTickets())
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

}
