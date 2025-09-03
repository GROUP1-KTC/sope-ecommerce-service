package com.sope.sope_ecommerce_backend.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        String title,
        String message,
        LocalDateTime createdAt
) {}
