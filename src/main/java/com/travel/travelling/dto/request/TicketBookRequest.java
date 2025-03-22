package com.travel.travelling.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketBookRequest {

    private String flightId; // Mã chuyến bay
    private String seatNumber; // Ghế cần đặt (VD: A1, B2)
}
