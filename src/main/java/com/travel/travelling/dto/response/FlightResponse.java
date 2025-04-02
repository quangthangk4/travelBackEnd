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
public class FlightResponse {
    private String id;

    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private double basePrice;
    private String status;
    private Long totalTickets;   // Tổng số vé có sẵn
    private Long soldTickets;    // Số vé đã bán

    private AircraftResponse airCraft;
}
