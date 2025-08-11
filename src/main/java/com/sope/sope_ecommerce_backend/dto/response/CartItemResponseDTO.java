package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.entities.ProductVariantEntity;

import java.util.UUID;

public record CartItemResponseDTO(
    UUID id,
   String productName,
   ProductVariantEntity productVariant,
    Integer quantity
) {
}
