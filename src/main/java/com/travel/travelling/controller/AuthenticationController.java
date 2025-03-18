package com.travel.travelling.controller;

import com.nimbusds.jose.JOSEException;
import com.travel.travelling.dto.request.AuthenticateRequest;
import com.travel.travelling.dto.request.IntrospectRequest;
import com.travel.travelling.dto.request.InvalidateTokenRequest;
import com.travel.travelling.dto.request.RefreshTokenRequest;
import com.travel.travelling.dto.response.ApiResponse;
import com.travel.travelling.dto.response.AuthenticateResponse;
import com.travel.travelling.dto.response.IntrospectResponse;
import com.travel.travelling.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ApiResponse<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest request) throws JOSEException {
        return ApiResponse.<AuthenticateResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }


    @PostMapping("/logout")
    public ApiResponse<Void> introspect(@RequestBody InvalidateTokenRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .message("Logout successful!")
                .build();
    }


    @PostMapping("/refreshToken")
    public ApiResponse<AuthenticateResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticateResponse>builder()
                .result(authenticationService.refreshToken(request))
                .build();
    }
}
