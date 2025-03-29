package com.travel.travelling.controller;

import com.travel.travelling.constant.VietnamAirport;
import com.travel.travelling.dto.response.ApiResponse;
import com.travel.travelling.dto.response.VietnamAirportResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/airports")
public class AirportController {

    @GetMapping("/vietnam")
    public ApiResponse<List<VietnamAirportResponse>> getVietnamAirports() {
        List<VietnamAirportResponse> airports = Arrays.stream(VietnamAirport.values())
                .map(airport -> new VietnamAirportResponse(
                     airport.getTenSanBay(),
                     airport.getViDo(),
                     airport.getKinhDo(),
                     airport.getQuocGia(),
                     airport.getMaQuocGia(),
                     airport.getMaIATA(),
                     airport.getTinhThanh()
                ))
                .collect(Collectors.toList());

        return ApiResponse.<List<VietnamAirportResponse>>builder()
                .result(airports)
                .build();
    }
}
