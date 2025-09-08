package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.entities.ShopAddress;

public record ShopUpdateRequest(
        String name,
        String phone,
        String email,
        ShopAddress address,
        String description,
        String logoUrl,
        Boolean isMall
) {}
