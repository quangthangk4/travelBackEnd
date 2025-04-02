package com.travel.travelling.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmPaymentRequest {
    private String flightIdArrival; // Mã chuyến bay
    private String flightIdReturn; // Mã chuyến về
    private String seatNumberArrival; // Ghế cần đặt (VD: A1, B2)
    private String seatNumberReturn; // Ghế cần đặt (VD: A1, B2)
    private Boolean isRoundTrip;
    private double totalPriceArrival;
    private double totalPriceReturn;
}


