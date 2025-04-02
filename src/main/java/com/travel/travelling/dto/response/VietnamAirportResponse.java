package com.travel.travelling.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VietnamAirportResponse {
    private String tenSanBay;
    private double viDo;
    private double kinhDo;
    private String quocGia;
    private String maQuocGia;
    private String maIATA;
    private String tinhThanh;
}
