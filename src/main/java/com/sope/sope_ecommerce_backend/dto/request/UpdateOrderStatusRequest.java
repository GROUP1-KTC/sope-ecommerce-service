package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdateOrderStatusRequest(
        UUID orderId,
        OrderStatus status
) {
}
