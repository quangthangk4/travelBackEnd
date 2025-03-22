package com.travel.travelling.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvalidateToken {
    @Id
    private String UUID;

    @Column(name = "expiryTime")
    private LocalDateTime expiryTime;
}
