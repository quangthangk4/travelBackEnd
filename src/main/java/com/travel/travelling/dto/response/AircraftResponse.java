package com.travel.travelling.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftResponse {
    private String id;
    private String name;
    private String model;
    private String manufacturer;
    private AirlineResponse airline;
}
