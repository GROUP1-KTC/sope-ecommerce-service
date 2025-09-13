package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;

import com.sope.sope_ecommerce_backend.enums.DiscountScope;

public record OrderDiscountResponse(
                String code,
                String description,
                BigDecimal discountAmount,
                DiscountScope scope) {
}
