package com.sope.sope_ecommerce_backend.dto.response;

public record PaymentResponse(
         boolean success,
         String providerPaymentId,
        String providerPayUrl,
        String rawResponse
) {
}
