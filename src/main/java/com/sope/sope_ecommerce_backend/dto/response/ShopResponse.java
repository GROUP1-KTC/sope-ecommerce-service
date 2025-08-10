package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.entities.Shop;

import java.util.UUID;

public record ShopResponse (
        UUID id,
        String name,
        String phone,
        String email,
        String address,
        String description,
        String logoUrl,
        boolean isMall,
        Shop.Status status
) {}
