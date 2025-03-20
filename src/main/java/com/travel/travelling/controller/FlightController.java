package com.travel.travelling.controller;

import com.travel.travelling.dto.request.FlightCreationRequest;
import com.travel.travelling.dto.request.FlightSearchRequest;
import com.travel.travelling.dto.response.AircraftResponse;
import com.travel.travelling.dto.response.ApiResponse;
import com.travel.travelling.dto.response.FlightResponse;
import com.travel.travelling.entity.Flight;
import com.travel.travelling.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    // create Flight
    @PostMapping("/create")
    public ApiResponse<FlightResponse> createFlight(@RequestBody FlightCreationRequest request) {
        return ApiResponse.<FlightResponse>builder()
                .message("create flight successfully")
                .result(flightService.createFlight(request))
                .build();
    }


    @DeleteMapping("/delete/{id}")
    public ApiResponse<FlightResponse> deleteFlight(@PathVariable String id) {
        flightService.deleteFlight(id);
        return ApiResponse.<FlightResponse>builder()
                .message("delete flight successfully")
                .build();
    }


    @GetMapping("/getAllFlight")
    public ApiResponse<List<FlightResponse>> getAllFlight() {
        return ApiResponse.<List<FlightResponse>>builder()
                .message("get all flight successfully")
                .result(flightService.getAllFlights())
                .build();
    }


    @GetMapping("/search")
    public ApiResponse<List<FlightResponse>> searchFlight(@RequestBody FlightSearchRequest request) {
        return ApiResponse.<List<FlightResponse>>builder()
                .message("search flight successfully")
                .result(flightService.searchFlight(request))
                .build();
    }
}
