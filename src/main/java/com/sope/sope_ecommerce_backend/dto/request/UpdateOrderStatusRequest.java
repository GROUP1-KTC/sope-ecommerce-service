package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.enums.OrderStatus;

import java.util.UUID;

public record UpdateOrderStatusRequest(
        UUID orderId,
        OrderStatus status
) {
}
