package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.response.ShopDTO;
import com.sope.sope_ecommerce_backend.entities.UserEntity;

import java.util.List;
import java.util.UUID;

public interface ShopService {

    ShopDTO createShop(ShopDTO shopDTO, UserEntity user);
    ShopDTO getShopById(UUID id);
    List<ShopDTO> getAllShops();
    ShopDTO updateShop(UUID id, ShopDTO shopDTO);
    void deleteShop(UUID id);
}
