package com.travel.travelling.dto.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketBookedResponse {
    private Set<String> seats;
}
