package com.sope.sope_ecommerce_backend.dto.request;

import java.util.UUID;

public record OrderCreateRequest(
        UUID userId,
        UUID shippingAddressId,
        String note,
        String discountCodeId,
        String idempotencyKey
) {
}
