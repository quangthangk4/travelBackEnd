package com.travel.travelling.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Random;

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

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String status;

    @Builder.Default
    private int totalTickets = 240;   // Tổng số vé có sẵn
    private int soldTickets;    // Số vé đã bán

    @Builder.Default
    private double basePrice = generateRandomPrice();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airCraftId")
    private Aircraft airCraft;

    private static double generateRandomPrice() {
        Random random = new Random();
        double price = 1000000 + Math.round(random.nextDouble() * 900000);
        return Math.round(price / 50000.0) * 50000;
    }
}
