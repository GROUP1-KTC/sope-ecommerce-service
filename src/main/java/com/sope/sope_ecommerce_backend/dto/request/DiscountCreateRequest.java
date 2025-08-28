package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.enums.DiscountScope;
import com.sope.sope_ecommerce_backend.enums.DiscountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DiscountCreateRequest(
        @NotBlank(message = "Code is required")
        @Size(min = 1, max = 20, message = "Code must be between 1 and 5 characters")
        String code,

        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description,

        @NotNull(message = "Discount value is required")
        @Positive(message = "Discount value must be positive")
        BigDecimal value,

        @PositiveOrZero(message = "Minimum order value must be non-negative")
        BigDecimal minOrderValue,

        @PositiveOrZero(message = "Max discount value must be non-negative")
        BigDecimal maxDiscountValue,

        @NotNull(message = "Discount type is required")
        DiscountType discountType,

        @Min(value = 0, message = "Max usage must be non-negative")
        int maxUsage,

        LocalDateTime startDate,

        LocalDateTime endDate,

        @NotNull(message = "Discount scope is required")
        DiscountScope scope,

        @PositiveOrZero(message = "Max coins must be non-negative")
        BigDecimal maxCoins
) {
}
