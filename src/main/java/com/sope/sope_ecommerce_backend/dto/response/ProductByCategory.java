package com.sope.sope_ecommerce_backend.dto.response;

import java.util.List;
import java.util.UUID;

public record ProductByCategory(
                UUID productId,
                String name,
                String slug,
                String brand,
                String defaultImage,
                List<ProductVariantByCategory> variantsByCategory) {
}