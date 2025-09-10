package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.entities.Shop;
import com.sope.sope_ecommerce_backend.entities.ShopAddress;

import java.util.UUID;

public record ShopResponse (
        UUID id,
        String name,
        String phone,
        String email,
        ShopAddressResponse address,
        String description,
        String logoUrl,
        boolean isMall,
        Shop.Status status
) {}
