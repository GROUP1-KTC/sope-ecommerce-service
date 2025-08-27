package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;

public record OrderDiscountResponse(
        String code,
        String discription,
        BigDecimal discountAmount
) {
}
