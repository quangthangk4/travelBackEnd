package com.travel.travelling.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvalidateToken {
    @Id
    private String UUID;
    private LocalDateTime expiryTime;
}
