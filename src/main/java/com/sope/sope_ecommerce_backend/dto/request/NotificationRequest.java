package com.sope.sope_ecommerce_backend.dto.request;

import java.util.List;
import java.util.UUID;

public record NotificationRequest(
        String title,
        String message,
        String link,
        List<UUID> userIds
) {}
