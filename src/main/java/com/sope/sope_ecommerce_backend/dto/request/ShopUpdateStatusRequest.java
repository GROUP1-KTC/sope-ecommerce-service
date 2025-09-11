package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.entities.Shop;

import java.util.UUID;

public record ShopUpdateStatusRequest(
        UUID shopId,
        Shop.Status status,
        String note
) {
}
