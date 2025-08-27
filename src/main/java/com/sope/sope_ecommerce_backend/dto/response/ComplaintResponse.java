package com.sope.sope_ecommerce_backend.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.enums.ComplaintStatus;
import com.sope.sope_ecommerce_backend.enums.ComplaintTargetType;

public record ComplaintResponse(
                UUID id,
                UUID reporterId,
                String reporterUsername,

                ComplaintTargetType targetType,
                UUID targetId,

                String reason,
                ComplaintStatus status,

                LocalDateTime createdAt,
                LocalDateTime solvedAt) {
}
