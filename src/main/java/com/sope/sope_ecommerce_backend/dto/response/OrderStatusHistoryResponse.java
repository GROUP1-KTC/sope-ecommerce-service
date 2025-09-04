package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderStatusHistoryResponse(
        OrderStatus status,
        LocalDateTime timestamp
) {
}
