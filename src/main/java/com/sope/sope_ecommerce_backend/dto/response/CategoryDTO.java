package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;

public record CategoryDTO(
            UUID id,
            String name,
            String slug,
            CategoryParentInfo parent) {
}