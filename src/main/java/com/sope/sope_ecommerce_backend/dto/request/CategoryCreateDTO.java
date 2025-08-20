package com.sope.sope_ecommerce_backend.dto.request;

import java.util.UUID;

public record CategoryCreateDTO(
            String name,
            UUID parentId) {
}
