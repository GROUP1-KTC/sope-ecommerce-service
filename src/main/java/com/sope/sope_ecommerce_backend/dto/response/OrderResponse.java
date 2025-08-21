package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderResponse(
        UUID orderId,
        UUID userId,
        UUID shippingAddressId,
        LocalDateTime orderDate,
        BigDecimal subtotal,
        BigDecimal shippingCharges,
        BigDecimal totalAmount,
        String note,
        OrderStatus status,
        List<OrderItemResponse> orderItems, // Nested DTO
        String idempotencyKey,
        String discountCodeId,

        String orderNumber
) {
}
