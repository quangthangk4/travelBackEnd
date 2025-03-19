package com.travel.travelling.controller;

import com.travel.travelling.dto.request.AircraftCreationRequest;
import com.travel.travelling.dto.response.AircraftResponse;
import com.travel.travelling.dto.response.AirlineResponse;
import com.travel.travelling.dto.response.ApiResponse;
import com.travel.travelling.entity.Aircraft;
import com.travel.travelling.service.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/aircraft")
public class AircraftController {

    private final AircraftService aircraftService;

    @PostMapping("/create")
    public ApiResponse<AircraftResponse> createAircraft(@RequestBody AircraftCreationRequest request) {
        return ApiResponse.<AircraftResponse>builder()
                .message("create aircraft successful")
                .result(aircraftService.createAircraft(request))
                .build();
    }


    @GetMapping("/getAllAircraft")
    public ApiResponse<List<AircraftResponse>> getAllAircraft() {
        return ApiResponse.<List<AircraftResponse>>builder()
                .message("getAllAirline successfully")
                .result(aircraftService.getAllAircraft())
                .build();
    }


    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteAircraft(@PathVariable String id) {
        aircraftService.deleteAircraft(id);
        return ApiResponse.<Void>builder()
                .message("delete aircraft successfully")
                .build();
    }
}
