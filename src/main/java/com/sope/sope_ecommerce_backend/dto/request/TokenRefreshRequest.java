package com.sope.sope_ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
        String refreshToken
) {}