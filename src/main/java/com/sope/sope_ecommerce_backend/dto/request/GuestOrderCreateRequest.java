package com.sope.sope_ecommerce_backend.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record GuestOrderCreateRequest(
        String idempotencyKey,
        GuestInfo guestInfo,
        List<OrderItemRequest> items,
        String paymentMethod,
        BigDecimal shippingCharge

        ) implements OrderCreateRequest {
}
