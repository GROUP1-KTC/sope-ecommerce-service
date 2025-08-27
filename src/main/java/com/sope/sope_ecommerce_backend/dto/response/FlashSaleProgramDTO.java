package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.sope.sope_ecommerce_backend.enums.ProgramStatus;

public record FlashSaleProgramDTO(
		String id,
		String productVariantId,
		BigDecimal discountPercentage,
		BigDecimal platformFeePercentage,
		ProgramStatus status,
		LocalDate saleDate,
		LocalTime startTime,
		LocalTime endTime,
		boolean active) {
}