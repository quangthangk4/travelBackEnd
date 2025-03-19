package com.travel.travelling.service;

import com.travel.travelling.dto.request.AircraftCreationRequest;
import com.travel.travelling.dto.response.AircraftResponse;
import com.travel.travelling.dto.response.AirlineResponse;
import com.travel.travelling.entity.Aircraft;
import com.travel.travelling.entity.Airline;
import com.travel.travelling.exception.AppException;
import com.travel.travelling.exception.ErrorCode;
import com.travel.travelling.mapper.AircraftMapper;
import com.travel.travelling.mapper.AirlineMapper;
import com.travel.travelling.repository.AircraftRepository;
import com.travel.travelling.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AircraftService {

    private final AirlineMapper airlineMapper;
    private final AircraftMapper aircraftMapper;
    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;

    // add Aircraft
    @PreAuthorize("hasRole('ADMIN')")
    public AircraftResponse createAircraft(AircraftCreationRequest request) {
        Aircraft aircraft = aircraftMapper.toAircraft(request);

        // tìm airline cho máy bay, nếu k có thì trả về lỗi
        Airline airline = airlineRepository.findByName(request.getAirlineName())
                .orElseThrow(() ->new AppException(ErrorCode.AIRLINE_NOT_EXISTED));

        aircraft.setAirline(airline);

        aircraft = aircraftRepository.save(aircraft);

        return aircraftMapper.toAircraftResponse(aircraft);
    }


    // Get all aircraft
    @PreAuthorize("hasRole('ADMIN')")
    public List<AircraftResponse> getAllAircraft() {
        return aircraftRepository.findAll().stream().map(aircraftMapper::toAircraftResponse).toList();
    }


    // delete aircraft
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAircraft(String aircraftId) {
        if(!aircraftRepository.existsById(aircraftId)) throw new AppException(ErrorCode.AIRCRAFT_NOT_EXISTED);
        aircraftRepository.deleteById(aircraftId);
    }
}
