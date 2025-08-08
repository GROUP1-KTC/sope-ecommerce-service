package com.sope.sope_ecommerce_backend.dto.request;

import java.util.UUID;

public record AddToCartRequestDTO(
        UUID productVariantId,
        int quantity
){}
