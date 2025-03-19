package com.travel.travelling.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingId implements Serializable {
    private String userId;
    private String flightId;
}
