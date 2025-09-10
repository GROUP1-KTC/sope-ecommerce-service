package com.sope.sope_ecommerce_backend.dto.response;

public record LiveEventMessage(
        String type,
        LiveStreamResponse payload
) {
}
