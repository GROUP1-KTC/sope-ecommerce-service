package com.sope.sope_ecommerce_backend.dto.response;

import java.time.Instant;
import java.util.UUID;

public record LiveStreamResponse(
        Long id,
        UUID shopId,
        String title,
        String description,
        String thumbnail,
        boolean isLive,
        Instant startedAt
) {
}
