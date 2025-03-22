package com.travel.travelling.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightTicketResponse {
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private AircraftTicketResponse airCraft;
}
