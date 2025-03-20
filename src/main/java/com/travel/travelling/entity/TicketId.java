package com.travel.travelling.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketId implements Serializable {
    private String userId;
    private String flightId;
}
