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

    private String seatNumber; // VD: A1, B2

    private LocalDateTime bookingDate;

    private boolean available;

    @Enumerated(EnumType.STRING)
    private TicketStatus status; // Trạng thái vé (ĐÃ ĐẶT, HỦY
    private double price; // Giá vé


    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", user=" + user +
                ", flight=" + flight +
                ", seatNumber='" + seatNumber + '\'' +
                ", bookingDate=" + bookingDate +
                ", available=" + available +
                ", status=" + status +
                ", price=" + price +
                '}';
    }
}
