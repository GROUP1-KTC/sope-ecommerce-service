package com.sope.sope_ecommerce_backend.exception;


import com.sope.sope_ecommerce_backend.utils.ApiResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("❌ Exception: {} at {}", ex.getMessage(), request.getDescription(false), ex);

        return ApiResponseUtil.internalError(
                "Internal server error",
                Collections.singletonList(ex.getMessage())
        );
    }
}
