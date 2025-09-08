package com.sope.sope_ecommerce_backend.services.impl;


import com.sope.sope_ecommerce_backend.dto.request.ShopAddressRequest;
import com.sope.sope_ecommerce_backend.dto.response.ShopAddressResponse;
import com.sope.sope_ecommerce_backend.entities.Shop;
import com.sope.sope_ecommerce_backend.entities.ShopAddress;
import com.sope.sope_ecommerce_backend.mapper.ShopAddressMapper;
import com.sope.sope_ecommerce_backend.repositories.ShopAddressRepository;
import com.sope.sope_ecommerce_backend.repositories.ShopRepository;
import com.sope.sope_ecommerce_backend.services.ShopAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopAddressServiceImpl implements ShopAddressService {

    private final ShopRepository shopRepository;
    private final ShopAddressRepository shopAddressRepository;
    private final ShopAddressMapper mapper;

    @Override
    public ShopAddressResponse updateAddress(UUID userId, UUID addressId, ShopAddressRequest request) {
        Shop shop = shopRepository.findByAppUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        ShopAddress address = shop.getAddress();
        if (address == null || !address.getId().equals(addressId)) {
            throw new RuntimeException("Address not found for this shop");
        }

        mapper.updateEntityFromDto(request, address);
        shopAddressRepository.save(address);

        return mapper.toResponse(address);
    }

    @Override
    public ShopAddressResponse getAddress(UUID userId) {
        Shop shop = shopRepository.findByAppUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        ShopAddress address = shop.getAddress();
        if (address == null) {
            throw new RuntimeException("Shop address not found");
        }

        return mapper.toResponse(address);
    }

    @Override
    public void deleteAddress(UUID userId, UUID addressId) {
        Shop shop = shopRepository.findByAppUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        ShopAddress address = shop.getAddress();
        if (address == null || !address.getId().equals(addressId)) {
            throw new RuntimeException("Address not found for this shop");
        }

        shop.setAddress(null);
        shopAddressRepository.delete(address);
    }
}
