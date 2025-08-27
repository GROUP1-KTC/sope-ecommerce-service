package com.sope.sope_ecommerce_backend.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

public record FlashSaleProgramRequest(
		UUID productVariantId,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate saleDate,
		@DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
		@DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
		BigDecimal discountPercentage) {
}