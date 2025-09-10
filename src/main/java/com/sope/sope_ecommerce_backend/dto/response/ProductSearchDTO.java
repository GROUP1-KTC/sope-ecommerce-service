package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;

public record ProductSearchDTO(
    UUID productId,
    String name,
    String defaultImage,
    String slug) {
}
