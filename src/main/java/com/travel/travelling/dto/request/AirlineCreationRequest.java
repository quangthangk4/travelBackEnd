package com.travel.travelling.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineCreationRequest {
    private String name;
    private String description;
    private String country;
    private String founded_year;
}
