package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;

public record ShopAddressResponse(
        UUID id,
        String street,
        String ward,
        String district,
        String city,
        String country,
        String zipCode
) {}
