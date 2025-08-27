package com.sope.sope_ecommerce_backend.dto.request;

import java.util.UUID;

import com.sope.sope_ecommerce_backend.enums.ComplaintTargetType;

public record ComplaintRequest(
        ComplaintTargetType targetType,
        UUID targetId,
        String reason) {
}
