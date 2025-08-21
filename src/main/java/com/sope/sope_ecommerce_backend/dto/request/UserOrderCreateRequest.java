package com.sope.sope_ecommerce_backend.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record UserOrderCreateRequest(
        UUID shippingAddressId,
        BigDecimal shippingCharge,
        String note,
        List<String> discountCodes,
        String idempotencyKey,
        List<OrderItemRequest> items,
        String paymentMethod
) implements OrderCreateRequest {
}
