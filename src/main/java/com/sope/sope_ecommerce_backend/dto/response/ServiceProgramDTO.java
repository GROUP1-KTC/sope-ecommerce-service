package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;

public record ServiceProgramDTO(
    BigDecimal dailyFeeForAds,
    BigDecimal platformFeePercentageForFlashSale) {
}