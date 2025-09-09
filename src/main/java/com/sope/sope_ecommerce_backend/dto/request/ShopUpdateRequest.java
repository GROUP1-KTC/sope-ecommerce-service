package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.dto.response.ShopAddressResponse;
import com.sope.sope_ecommerce_backend.entities.ShopAddress;

public record ShopUpdateRequest(
        String name,
        String phone,
        String email,
        ShopAddressResponse address,
        String description,
        String logoUrl,
        Boolean isMall
) {}
