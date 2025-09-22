package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.enums.Sentiment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReviewDTO(
        UUID reviewId,
        Integer rating,
        String content,
        UserInfo user,
        String videoReviewUrl,
        ProductVariantInfo productVariant,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ReviewMediaDTO> mediaList,
        Sentiment sentiment) {
}
