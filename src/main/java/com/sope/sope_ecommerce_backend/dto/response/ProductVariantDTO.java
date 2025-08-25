package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.entities.Dimension;

public record ProductVariantDTO(
            UUID productVariantId,
            BigDecimal price,
            int stock,
            int sold,
            UUID productId,
            List<AttributeDTO> attributes,
            String imageVariant,
            Dimension dimension,
            BigDecimal weight) {
}
