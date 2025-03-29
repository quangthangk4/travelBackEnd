package com.travel.travelling.service;

import com.travel.travelling.constant.VietnamAirport;
import com.travel.travelling.dto.request.FlightCreationRequest;
import com.travel.travelling.dto.request.FlightSearchRequest;
import com.travel.travelling.dto.response.AircraftResponse;
import com.travel.travelling.dto.response.FlightResponse;
import com.travel.travelling.entity.Aircraft;
import com.travel.travelling.entity.Flight;
import com.travel.travelling.exception.AppException;
import com.travel.travelling.exception.ErrorCode;
import com.travel.travelling.mapper.FlightMapper;
import com.travel.travelling.repository.AircraftRepository;
import com.travel.travelling.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {


    private final FlightRepository flightRepository;
    private final AircraftRepository aircraftRepository;
    private final FlightMapper flightMapper;

    // create flight
    @PreAuthorize("hasRole('ADMIN')")
    public FlightResponse createFlight(FlightCreationRequest request){
        // check flightNumber k trùng trong 1 khoảng thời gian
        boolean isDuplicateFlight = flightRepository.findAllByFlightNumber(request.getFlightNumber())
                .stream()
                .anyMatch(existingFlight -> {
                    LocalDateTime existingDeparture = existingFlight.getDepartureTime();
                    LocalDateTime existingArrival = existingFlight.getArrivalTime();
                    LocalDateTime newDeparture = request.getDepartureTime();
                    LocalDateTime newArrival = request.getArrivalTime();

                    // Kiểm tra xem chuyến bay mới có trùng trong vòng 24 giờ không
                    boolean isWithin24Hours =
                            (newDeparture.isAfter(existingDeparture.minusHours(24)) && newDeparture.isBefore(existingArrival.plusHours(24))) ||
                                    (newArrival.isAfter(existingDeparture.minusHours(24)) && newArrival.isBefore(existingArrival.plusHours(24)));

                    return isWithin24Hours;
                });

        if(isDuplicateFlight)
            throw new AppException(ErrorCode.FLIGHT_DUPLICATE_WITHIN_24H);

        // check điểm đến và điểm đi có hợp lệ không
        if(request.getDepartureAirport().equals(request.getArrivalAirport()))
            throw new AppException(ErrorCode.DEPARTURE_AND_DESTINATION_CANNOT_BE_SAME);

        if(!(VietnamAirport.isValidIataCode(request.getDepartureAirport())
                && VietnamAirport.isValidIataCode(request.getArrivalAirport())))
            throw new AppException(ErrorCode.DEPARTURE_OR_DESTINATION_INVALID);

        // check nếu thời điểm đi sau thời điểm đến :v
        if(request.getDepartureTime().isAfter(request.getArrivalTime()))
            throw new AppException(ErrorCode.DEPARTURE_TIME_AFTER_ARRIVAL_TIME);

        // thêm máy bay
        Aircraft aircraft = aircraftRepository.findById(request.getAircraftId()).orElseThrow(
                () -> new AppException(ErrorCode.AIRCRAFT_NOT_EXISTED));


        // map qua flight
        Flight flight = flightMapper.toFlight(request);
        flight.setAirCraft(aircraft);

        flight = flightRepository.save(flight);

        // lưu
        return flightMapper.toFlightResponse(flight);
    }


    // delete flight
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFlight(String id){
        if (flightRepository.findById(id).isEmpty()) throw new AppException(ErrorCode.FLIGHT_NOT_EXISTED);
        flightRepository.deleteById(id);
    }


    // Get all flight
    @PreAuthorize("hasRole('ADMIN')")
    public List<FlightResponse> getAllFlights() {
        return flightRepository.findAll().stream().map(flightMapper::toFlightResponse).toList();
    }


    // search flight
    public List<FlightResponse> searchFlight(FlightSearchRequest request){

        // chỉ lọc ra những chuyến bay sau 3h khởi hành máy bay
        List<Flight> flights = flightRepository.findAllByDepartureAirportAndArrivalAirport(request.getDepartureAirport(), request.getArrivalAirport());
        log.info("danh sách flights: {}", flights.stream().toList());


        flights = flights.stream().filter(flight ->
                            flight.getDepartureTime().toLocalDate().equals(request.getDepartureDate()) &&
                            Instant.now().isBefore(flight.getDepartureTime().minus(3, ChronoUnit.HOURS).toInstant(ZoneOffset.UTC))
                )
                .toList();


        return flights.stream().map(flightMapper::toFlightResponse).toList();
    }


    // lấy thông tin chuyenes bay
    public FlightResponse getFlight(String id){
        Flight flight = flightRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED));

        return flightMapper.toFlightResponse(flight);
    }


    // cập nhật thông tin chuyến bay như delay ....

}
