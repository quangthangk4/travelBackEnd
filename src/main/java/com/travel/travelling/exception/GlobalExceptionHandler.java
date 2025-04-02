package com.travel.travelling.exception;

import com.travel.travelling.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> duplicateEmail(DataIntegrityViolationException exception) {
        ErrorCode errorCode = ErrorCode.DUPLICATE_RESOURCE; // Chọn mã lỗi phù hợp
        String message = exception.getRootCause().getMessage(); // Lấy lỗi từ database

        String errorMessage = "Lỗi không thể xóa dữ liệu: ";
        if (message.contains("Duplicate entry")) {
            String duplicateValue = message.split("'")[1]; // Lấy giá trị bị trùng (Vietnam Airlines)
            errorMessage = duplicateValue + " already exists.";
        } else if (message.contains("foreign key constraint fails")) {
            // Tìm kiếm tên của bảng khóa ngoại (Ví dụ: "Aircraft")
            String[] parts = message.split("`");
            if (parts.length >= 3) {
                String relatedTable = parts[3];  // Tên bảng liên quan
                errorMessage += "Vui lòng xóa các " + relatedTable + " liên quan trước khi xóa.";
            } else {
                errorMessage += "Có sự phụ thuộc dữ liệu không thể xóa.";
            }}

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
