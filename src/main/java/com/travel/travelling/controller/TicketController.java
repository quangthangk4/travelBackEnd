package com.travel.travelling.controller;

import com.travel.travelling.dto.request.TicketBookRequest;
import com.travel.travelling.dto.response.ApiResponse;
import com.travel.travelling.dto.response.TicketResponse;
import com.travel.travelling.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class TicketController {


    private final TicketService ticketService;

    @PostMapping("/book")
    public ApiResponse<TicketResponse> createTickets(@RequestBody TicketBookRequest request){
        return ApiResponse.<TicketResponse>builder()
                .message("book ticket successfully")
                .result(ticketService.createTickets(request))
                .build();
    }
}
