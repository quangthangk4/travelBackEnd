package com.travel.travelling.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @EmbeddedId
    private BookingId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    private User user;


    @ManyToOne
    @MapsId("flightId")
    @JoinColumn(name = "flightId")
    private Flight flight;

    private LocalDateTime bookingDate;
}
