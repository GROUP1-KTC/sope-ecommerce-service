package com.sope.sope_ecommerce_backend.utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sope.sope_ecommerce_backend.dto.ApiResponse;

import java.util.Collections;
import java.util.List;

public class ApiResponseUtil {
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return build(true, message, data, null, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return build(true, message, data, null, HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(List<String> errors) {
        return build(false, "Bad Request", null, errors, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return build(false, message, null, Collections.singletonList(message), HttpStatus.NOT_FOUND);
    }

    public static <T> ResponseEntity<ApiResponse<T>> internalError(String message, List<String> errors) {
        return build(false, message, null, errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        return build(false, message, null, Collections.singletonList(message), HttpStatus.UNAUTHORIZED);
    }

    private static <T> ResponseEntity<ApiResponse<T>> build(
            boolean success,
            String message,
            T data,
            List<String> errors,
            HttpStatus status
    ) {
        ApiResponse<T> response = new ApiResponse<>(
                success,
                message,
                data,
                errors,
                status.value()
        );
        return new ResponseEntity<>(response, status);
    }
}
