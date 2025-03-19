package com.travel.travelling.exception;

import com.travel.travelling.dto.response.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> duplicateEmail(DataIntegrityViolationException exception) {
        ErrorCode errorCode = ErrorCode.DUPLICATE_RESOURCE; // Chọn mã lỗi phù hợp
        String message = exception.getRootCause().getMessage(); // Lấy lỗi từ database

        String errorMessage;
        if (message.contains("Duplicate entry")) {
            String duplicateValue = message.split("'")[1]; // Lấy giá trị bị trùng (Vietnam Airlines)
            errorMessage = duplicateValue + " already exists.";
        } else {
            errorMessage = "Database constraint violation: " + message;
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorMessage)
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }


    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> appException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> accessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }
}
