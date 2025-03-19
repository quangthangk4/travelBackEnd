package com.travel.travelling.mapper;

import com.travel.travelling.dto.request.AirlineCreationRequest;
import com.travel.travelling.dto.response.AirlineResponse;
import com.travel.travelling.entity.Airline;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AirlineMapper {
    AirlineResponse toAirlineResponse(Airline airline);

    Airline toAirline(AirlineCreationRequest airlineCreationRequest);
}
