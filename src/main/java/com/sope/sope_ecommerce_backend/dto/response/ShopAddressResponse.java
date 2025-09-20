package com.sope.sope_ecommerce_backend.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ShopAddressResponse(
        UUID id,
        String street,
        String ward,
        String district,
        String city,
        String country
) {}
