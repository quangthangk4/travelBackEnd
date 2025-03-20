package com.travel.travelling.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    DUPLICATE_RESOURCE(1001, " already exists.", HttpStatus.CONFLICT),
    USER_NOT_EXISTED(1002, "user not existed!", HttpStatus.NOT_FOUND),
    PASSWORD_INCORRECT(1003, "password incorrect!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "unauthenticated!", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_LOGOUT(1005, "This token has already been logged out!", HttpStatus.CONFLICT),
    INVALID_TOKEN(1006, "invalid token!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission!", HttpStatus.FORBIDDEN),
    AIRLINE_NOT_EXISTED(1008, "airline not existed!", HttpStatus.NOT_FOUND),
    AIRCRAFT_NOT_EXISTED(1009, "aircraft not existed!", HttpStatus.NOT_FOUND),
    FLIGHT_NOT_EXISTED(1013, "flight not existed!", HttpStatus.NOT_FOUND),
    FLIGHT_DUPLICATE_WITHIN_24H(1010, "Flight with FlightNumber is duplicated " +
            "within 24 hours!, Vui lòng tạo chuyến bay sau 24h so với chuyến bay trước",
            HttpStatus.BAD_REQUEST),
    DEPARTURE_AND_DESTINATION_CANNOT_BE_SAME(1011, "Departure and destination cannot be the same!", HttpStatus.BAD_REQUEST),
    DEPARTURE_OR_DESTINATION_INVALID(1012, "Departure or destination is invalid!", HttpStatus.BAD_REQUEST),
    DEPARTURE_TIME_AFTER_ARRIVAL_TIME(1014, "Departure time must be before arrival time!", HttpStatus.BAD_REQUEST),


    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
