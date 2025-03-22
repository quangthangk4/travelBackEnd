package com.travel.travelling.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketId implements Serializable {
    @Column(name = "userId")
    private String userId;

    @Column(name = "flightId")
    private String flightId;
}
