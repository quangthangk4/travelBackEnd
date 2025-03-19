package com.travel.travelling.mapper;

import com.travel.travelling.dto.request.AircraftCreationRequest;
import com.travel.travelling.dto.response.AircraftResponse;
import com.travel.travelling.entity.Aircraft;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AircraftMapper {

    @Mapping(target = "airline", ignore = true)
    Aircraft toAircraft(AircraftCreationRequest request);

    AircraftResponse toAircraftResponse(Aircraft aircraft);
}
