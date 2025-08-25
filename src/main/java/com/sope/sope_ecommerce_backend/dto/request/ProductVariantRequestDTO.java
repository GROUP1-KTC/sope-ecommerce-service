package com.sope.sope_ecommerce_backend.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.dto.response.AttributeDTO;
import com.sope.sope_ecommerce_backend.entities.Dimension;

public record ProductVariantRequestDTO(
            UUID productVariantId,
            BigDecimal price,
            Integer stock,
            List<AttributeDTO> attributes,
            String imageVariant,
            Dimension dimension,
            BigDecimal weight) {
}