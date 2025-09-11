package com.sope.sope_ecommerce_backend.dto.request;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

public record AdsProgramRequest(
        UUID productId,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        int durationDays) {
}
