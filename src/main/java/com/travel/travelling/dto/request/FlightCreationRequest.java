package com.travel.travelling.dto.request;

import com.travel.travelling.dto.response.AircraftResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightCreationRequest {

    private String flightNumber;

    private String departureAirport;
    private String arrivalAirport;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String status;

    private String aircraftId;
    private double basePrice;
}
