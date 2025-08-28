package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        UUID orderId,
        String requestId,

        BigDecimal amount,
        PaymentMethod method,
        PaymentProvider provider,
        String currency,
        String orderInfo,
        String tempOrderCode
) {
}
