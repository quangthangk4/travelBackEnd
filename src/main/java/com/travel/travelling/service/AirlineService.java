package com.travel.travelling.service;

import com.travel.travelling.dto.request.AirlineCreationRequest;
import com.travel.travelling.dto.response.AirlineResponse;
import com.travel.travelling.entity.Airline;
import com.travel.travelling.exception.AppException;
import com.travel.travelling.exception.ErrorCode;
import com.travel.travelling.mapper.AirlineMapper;
import com.travel.travelling.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirlineService {

    private final AirlineMapper airlineMapper;
    private final AirlineRepository airlineRepository;

    // create Airline (tạo hãng hàng khong)
    @PreAuthorize("hasRole('ADMIN')")
    public AirlineResponse createAirline(AirlineCreationRequest request) {
        Airline airline = airlineMapper.toAirline(request);

        airline = airlineRepository.save(airline);

        log.info("đã lưu vào database");
        return airlineMapper.toAirlineResponse(airline);
    }


    // get All Airline
    @PreAuthorize("hasRole('ADMIN')")
    public List<AirlineResponse> getAllAirlines() {
        return airlineRepository.findAll().stream().map(airlineMapper::toAirlineResponse).toList();
    }

    // delete airline
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAirline(String airlineId) {
        if(!airlineRepository.existsById(airlineId)) throw new AppException(ErrorCode.AIRLINE_NOT_EXISTED);
        airlineRepository.deleteById(airlineId);
    }
}
