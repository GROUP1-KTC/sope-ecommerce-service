package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.entities.Dimension;

public record ProductVariantDetailDTO(
            UUID productVariantId,
            BigDecimal price,
            int stock,
            int sold,
            String imageVariant,
            List<AttributeDTO> attributes,
            ProductInfo product,
            Dimension dimension,
            BigDecimal weight) {
      public record ProductInfo(
                  UUID productId,
                  String name) {
      }
}
