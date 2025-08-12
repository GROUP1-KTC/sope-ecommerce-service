package com.sope.sope_ecommerce_backend.dto.response;

public record PaymentResponse(
         boolean success,
         String providerPaymentId,
        String payUrl, // redirect URL hoặc qr code data
        String rawResponse
) {
}
