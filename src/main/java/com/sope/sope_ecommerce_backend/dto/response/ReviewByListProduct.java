package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;

public record ReviewByListProduct(
        UUID reviewId,
        Integer rating,
        ProductVariantInfo productVariant) {
}
