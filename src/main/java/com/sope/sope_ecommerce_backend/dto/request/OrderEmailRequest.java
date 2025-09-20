package com.sope.sope_ecommerce_backend.dto.request;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderEmailRequest(
        String code,
        LocalDateTime date,
        String status,
        List<OrderItemEmailRequest> items,
        BigDecimal totalAmount,
        String guestName,
        String guestEmail,
        String guestPhone,
        String guestAddress
) {
}
