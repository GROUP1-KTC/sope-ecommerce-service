package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;

public record ProductInfo(
            UUID productId,
            String name) {
}