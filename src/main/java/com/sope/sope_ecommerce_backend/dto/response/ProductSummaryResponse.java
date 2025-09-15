package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;
import java.math.BigDecimal;

public record ProductSummaryResponse(
        UUID productId,
        String name,
        String slug,
        BigDecimal minPrice,
        String defaultImage,
        int totalStock,
        int totalSold,
        Double averageRating) {
}