package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.enums.MediaType;

public record ReviewMediaDTO(
            Long id,
            String url,
            int priority,
            MediaType type) {
}