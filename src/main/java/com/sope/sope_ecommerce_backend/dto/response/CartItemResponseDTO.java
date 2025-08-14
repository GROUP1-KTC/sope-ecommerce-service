package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.entities.ProductVariant;

import java.util.UUID;

public record CartItemResponseDTO(
    UUID id,
   String productName,
   ProductVariant productVariant,
    Integer quantity
) {
}
