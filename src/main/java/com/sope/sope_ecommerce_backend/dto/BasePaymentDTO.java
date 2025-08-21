package com.sope.sope_ecommerce_backend.dto;

public record BasePaymentDTO(
        String providerPayUrl,
        String providerPaymentId
) {}