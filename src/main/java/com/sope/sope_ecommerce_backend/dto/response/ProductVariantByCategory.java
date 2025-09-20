package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;

public record ProductVariantByCategory(
                BigDecimal price,
                int sold,
                int stock) {
}
