package com.sope.sope_ecommerce_backend.dto.request;

public record ShopAddressRequest(
        String street,
        String ward,
        String district,
        String city,
        String country,
        String zipCode
) {}
