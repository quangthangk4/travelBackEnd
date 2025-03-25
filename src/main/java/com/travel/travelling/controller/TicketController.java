package com.travel.travelling.controller;

import com.travel.travelling.dto.request.TicketBookRequest;
import com.travel.travelling.dto.response.ApiResponse;
import com.travel.travelling.dto.response.TicketResponse;
import com.travel.travelling.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {


    private final TicketService ticketService;

    @PostMapping("/book")
    public ApiResponse<TicketResponse> createTickets(@RequestBody TicketBookRequest request){
        return ApiResponse.<TicketResponse>builder()
                .message("book ticket successfully")
                .result(ticketService.bookTicket(request))
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
}
