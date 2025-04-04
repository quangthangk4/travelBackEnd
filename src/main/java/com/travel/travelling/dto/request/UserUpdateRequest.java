package com.travel.travelling.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateRequest {
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String cccd;
    private LocalDate birthday;
}