package com.travel.travelling.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    DATA_CONFUSE(1001, "Dữ liệu đã tồn tại!", HttpStatus.CONFLICT),
    USER_NOT_EXISTED(1002, "user not existed!", HttpStatus.NOT_FOUND),
    PASSWORD_INCORRECT(1003, "password incorrect!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "unauthenticated!", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_LOGOUT(1005, "This token has already been logged out!", HttpStatus.CONFLICT),
    INVALID_TOKEN(1006, "invalid token!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission!", HttpStatus.FORBIDDEN);
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
