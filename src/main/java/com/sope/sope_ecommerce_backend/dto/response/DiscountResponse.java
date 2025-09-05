package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.enums.DiscountScope;
import com.sope.sope_ecommerce_backend.enums.DiscountStatus;
import com.sope.sope_ecommerce_backend.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DiscountResponse(
        UUID id,
        String code,
        String description,
        BigDecimal value,
        BigDecimal minOrderValue,
        BigDecimal maxDiscountValue,
        DiscountType discountType,
        int maxUsage,
        int currentUsage,
        LocalDateTime createdAt,
        LocalDateTime startDate,
        LocalDateTime endDate,
        DiscountScope scope,
        BigDecimal maxCoins,
        DiscountStatus status

) {
}
