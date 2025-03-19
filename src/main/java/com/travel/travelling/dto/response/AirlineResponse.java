package com.travel.travelling.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineResponse {
    private String id;
    private String name;
    private String description;
    private String country;
    private String founded_year;
}
