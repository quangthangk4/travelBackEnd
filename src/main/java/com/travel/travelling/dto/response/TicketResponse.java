package com.travel.travelling.dto.response;

import com.travel.travelling.constant.TicketStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponse {

    private UserTicketReponse user;

    private FlightTicketResponse flight;

    private String seatNumber; // VD: A1, B2

    private LocalDateTime bookingDate;

    private boolean available;

    @Enumerated(EnumType.STRING)
    private TicketStatus status; // Trạng thái vé (ĐÃ ĐẶT, HỦY
    private double price; // Giá vé
}
