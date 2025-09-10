package com.sope.sope_ecommerce_backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReviewDTO(
        UUID reviewId,
        Integer rating,
        String content,
        UserInfo user,
        ProductVariantInfo productVariant,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ReviewMediaDTO> mediaList) {
}
