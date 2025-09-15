package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record VariantInfo(
    String productVariantId,
    String imageUrl,
    String productName,
    BigDecimal price,
    int stock,
    int sold,
    List<AttributeDTO> attributes) {
}
