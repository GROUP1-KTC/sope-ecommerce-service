package com.sope.sope_ecommerce_backend.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserNotificationResponse(
        UUID id,
        NotificationResponse notification,
        boolean isRead,
        LocalDateTime readAt
) {}
