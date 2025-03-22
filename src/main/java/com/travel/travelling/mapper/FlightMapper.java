package com.travel.travelling.mapper;


import com.travel.travelling.dto.request.FlightCreationRequest;
import com.travel.travelling.dto.response.FlightResponse;
import com.travel.travelling.dto.response.FlightTicketResponse;
import com.travel.travelling.entity.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    @Mapping(target = "airCraft", ignore = true)
    Flight toFlight(FlightCreationRequest request);

    FlightResponse toFlightResponse(Flight flight);

    FlightTicketResponse toFlightTicketResponse(Flight flight);
}
