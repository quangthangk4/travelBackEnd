package com.travel.travelling.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntrospectResponse {
    @Builder.Default
    private boolean valid = false;
}
