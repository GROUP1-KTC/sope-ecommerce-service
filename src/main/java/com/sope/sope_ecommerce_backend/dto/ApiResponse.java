package com.sope.sope_ecommerce_backend.dto;

import java.time.Instant;
import java.util.List;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        List<String> errors,
        int statusCode,
        String timestamp
) {
    public ApiResponse(
            boolean success,
            String message,
            T data,
            List<String> errors,
            int statusCode
    ) {
        this(success, message, data, errors, statusCode, Instant.now().toString());
    }
}
