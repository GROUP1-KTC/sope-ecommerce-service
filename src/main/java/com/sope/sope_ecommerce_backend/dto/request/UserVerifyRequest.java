package com.sope.sope_ecommerce_backend.dto.request;

public record UserVerifyRequest (
        String email,
        String otp
) {}
