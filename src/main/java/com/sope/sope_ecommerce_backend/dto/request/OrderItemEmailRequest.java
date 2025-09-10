package com.sope.sope_ecommerce_backend.dto.request;

import lombok.Builder;

import java.math.BigDecimal;


@Builder
public record OrderItemEmailRequest(
        String name,
        int quantity,
        BigDecimal price
) {
}
