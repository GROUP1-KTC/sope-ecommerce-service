package com.sope.sope_ecommerce_backend.dto.response;

import java.time.Instant;

public record LiveStreamResponse(
        Long id,
        String title,
        String description,
        String thumbnail,
        boolean isLive,
        Instant startAt
) {
}
