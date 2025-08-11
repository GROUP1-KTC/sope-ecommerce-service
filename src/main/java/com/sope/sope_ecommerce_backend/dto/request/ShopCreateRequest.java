package com.sope.sope_ecommerce_backend.dto.request;

public record ShopCreateRequest (
        String name,
        String phone,
        String email,
        String address,
        String description,
        String logoUrl,
        boolean isMall
) {}
