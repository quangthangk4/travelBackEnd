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
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private String destination;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String status;

    private double price; // Giá vé
    private Long countTickets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airCraftId")
    private Aircraft airCraft;
}
