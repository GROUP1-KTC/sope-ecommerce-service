package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AdsProgramDTO(
        String id,
        String productId,
        BigDecimal dailyFee,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        boolean active) {
}