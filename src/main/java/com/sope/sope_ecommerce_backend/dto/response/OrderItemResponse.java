package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(

        UUID productVariantId,
        int quantity,
        BigDecimal price
) {}
