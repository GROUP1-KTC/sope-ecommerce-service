package com.sope.sope_ecommerce_backend.dto.response;

import java.util.List;
import java.util.UUID;

public record ProductAdsDTO(
    UUID productId,
    String name,
    String defaultImage,
    String slug,
    List<ProductVariantDTO> variants) {
}