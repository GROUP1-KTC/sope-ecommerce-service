package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.AddressCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.AddressUpdateRequest;
import com.sope.sope_ecommerce_backend.dto.response.AddressResponse;
import com.sope.sope_ecommerce_backend.entities.Address;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    AddressResponse addAddress(UUID userId, AddressCreateRequest request);

    Address getOrCreateAddress(UUID userId, AddressCreateRequest request);

    AddressResponse updateAddress(UUID userId, UUID addressId, AddressUpdateRequest request);
    void deleteAddress(UUID userId, UUID addressId);
    List<AddressResponse> getUserAddresses(UUID userId);
    AddressResponse setDefaultAddress(UUID userId, UUID addressId);
    AddressResponse getDefaultAddress(UUID userId);

    Address getAddressEntityById(UUID addressId) ;
}
