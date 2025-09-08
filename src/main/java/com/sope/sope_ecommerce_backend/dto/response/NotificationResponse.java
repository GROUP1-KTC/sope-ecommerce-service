package com.sope.sope_ecommerce_backend.dto.response;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        String title,
        String message,
        Instant createdAt
) {}
