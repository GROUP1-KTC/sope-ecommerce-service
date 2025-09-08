package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.ShopAddressRequest;
import com.sope.sope_ecommerce_backend.dto.response.ShopAddressResponse;

import java.util.UUID;

public interface ShopAddressService {

    ShopAddressResponse updateAddress(UUID userId, UUID addressId, ShopAddressRequest request);

    ShopAddressResponse getAddress(UUID userId);

    void deleteAddress(UUID userId, UUID addressId);
}

