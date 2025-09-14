package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AdsProgramDTO(
                String id,
                ProductAdsDTO productInfo,
                BigDecimal dailyFee,
                LocalDate startDate,
                LocalDate endDate,
                String status,
                boolean active) {
}