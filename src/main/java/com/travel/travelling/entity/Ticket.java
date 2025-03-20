package com.travel.travelling.entity;

import com.travel.travelling.constant.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {

    @EmbeddedId
    private TicketId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    private User user;


    @ManyToOne
    @MapsId("flightId")
    @JoinColumn(name = "flightId")
    private Flight flight;

    private LocalDateTime bookingDate;

    private String seatNumber; // Số ghế ngồi

    @Enumerated(EnumType.STRING)
    private TicketStatus status; // Trạng thái vé (ĐÃ ĐẶT, HỦY, CHECK-IN)
    private LocalDateTime expirationTime;
    private double price; // Giá vé
}
