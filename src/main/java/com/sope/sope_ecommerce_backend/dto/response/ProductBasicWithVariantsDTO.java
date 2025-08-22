package com.sope.sope_ecommerce_backend.dto.response;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Builder;

@Builder
public record ProductBasicWithVariantsDTO(
            UUID productId,
            String name,
            List<Map<String, String>> variants) {
}