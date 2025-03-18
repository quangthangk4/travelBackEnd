package com.travel.travelling.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String cccd;
    private LocalDate birthday;
}