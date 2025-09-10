package com.sope.sope_ecommerce_backend.dto.response;

import java.util.List;
import java.util.UUID;

public record ProductVariantInfo(
    UUID productVariantId,
    List<AttributeDTO> attributes) {
}