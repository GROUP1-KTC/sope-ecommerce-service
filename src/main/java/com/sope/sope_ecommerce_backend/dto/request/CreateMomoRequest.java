package com.sope.sope_ecommerce_backend.dto.request;


import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateMomoRequest(
        String partnerCode,
        String requestType,
        String ipnUrl,
        String redirectUrl,
        String orderId,

        String amount,
        String orderInfo,
        String requestId, // idempotency key
        String extraData,
        String signature, // HMAC signature
        String lang
) {
}
