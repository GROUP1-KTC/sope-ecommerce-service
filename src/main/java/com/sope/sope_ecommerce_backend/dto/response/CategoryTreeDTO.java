package com.sope.sope_ecommerce_backend.dto.response;

import java.util.List;
import java.util.UUID;

public record CategoryTreeDTO(
    UUID id,
    String name,
    String slug,
    Integer level,
    UUID parentId,
    List<CategoryTreeDTO> children) {
}
