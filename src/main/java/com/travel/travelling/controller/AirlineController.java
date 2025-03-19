package com.travel.travelling.controller;

import com.travel.travelling.dto.request.AirlineCreationRequest;
import com.travel.travelling.dto.response.AirlineResponse;
import com.travel.travelling.dto.response.ApiResponse;
import com.travel.travelling.entity.Airline;
import com.travel.travelling.service.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/airline")
public class AirlineController {

    private final AirlineService airlineService;

    // create Airline
    @PostMapping("/create")
    public ApiResponse<AirlineResponse> createAirline(@RequestBody AirlineCreationRequest request) {
        return ApiResponse.<AirlineResponse>builder()
                .message("create airline successfully")
                .result(airlineService.createAirline(request))
                .build();
    }

    @GetMapping("/getAllAirline")
    public ApiResponse<List<AirlineResponse>> getAllAirline() {
        return ApiResponse.<List<AirlineResponse>>builder()
                .message("getAllAirline successfully")
                .result(airlineService.getAllAirlines())
                .build();
    }


    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteAirline(@PathVariable String id) {
        airlineService.deleteAirline(id);
        return ApiResponse.<Void>builder()
                .message("delete airline successfully")
                .build();
    }
}
