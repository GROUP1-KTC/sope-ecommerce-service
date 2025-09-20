package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;

public record ProductDetailDTO(
        UUID productDetailId,
        String label,
        String data,
        int priority) {
}
