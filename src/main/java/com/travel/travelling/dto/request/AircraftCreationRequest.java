package com.travel.travelling.dto.request;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftCreationRequest {

    private String name;
    private String model;
    private String manufacturer;
    private String airlineName;
}
