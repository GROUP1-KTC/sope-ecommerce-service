package com.sope.sope_ecommerce_backend.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserNotificationResponse(
        UUID id,
        NotificationResponse notification,
        boolean isRead,
        Instant readAt
) {}
