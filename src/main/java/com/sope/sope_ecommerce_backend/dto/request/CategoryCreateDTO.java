package com.sope.sope_ecommerce_backend.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record CategoryCreateDTO(
    String name,
    BigDecimal commissionFeePercent,
    UUID parentId) {
}
