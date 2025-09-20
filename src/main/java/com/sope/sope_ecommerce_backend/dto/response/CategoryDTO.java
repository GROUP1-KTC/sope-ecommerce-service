package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CategoryDTO(
        UUID id,
        String name,
        String slug,
        Integer level,
        BigDecimal commissionFeePercent,
        UUID parentId,
        String imageForParent) {
}