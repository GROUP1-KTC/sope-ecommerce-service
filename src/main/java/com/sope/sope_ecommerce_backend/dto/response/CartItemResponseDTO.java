package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponseDTO(
                UUID id,
                String productName,
                UUID productVariantId,
                BigDecimal price,
                String imageVariant,
                Integer quantity) {
}
