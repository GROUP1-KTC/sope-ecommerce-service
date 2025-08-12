package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        UUID orderId,
        BigDecimal amount,
         PaymentMethod method,
         String currency,
         String returnUrl, // redirect after pay (optional)
         String notifyUrl, // webhook callback
         String idempotencyKey
) {
}
