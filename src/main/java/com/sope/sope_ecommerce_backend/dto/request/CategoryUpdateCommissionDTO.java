package com.sope.sope_ecommerce_backend.dto.request;

import java.math.BigDecimal;

public record CategoryUpdateCommissionDTO(
    BigDecimal commissionFeePercent) {
}
