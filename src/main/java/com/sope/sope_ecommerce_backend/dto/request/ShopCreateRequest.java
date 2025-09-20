package com.sope.sope_ecommerce_backend.dto.request;

public record ShopCreateRequest (
        String name,
        String phone,
        String email,
        ShopAddressRequest address,
        String description,
        String logoUrl,
        String taxCode,
        String taxDocumentUrl,
        ShopIdentificationRequest identification,
        boolean isMall
) {}
