package com.sope.sope_ecommerce_backend.modules.shop.service;

import com.sope.sope_ecommerce_backend.modules.shop.dto.ShopDTO;
import com.sope.sope_ecommerce_backend.modules.user.entity.User;

import java.util.List;
import java.util.UUID;

public interface ShopService {

    ShopDTO createShop(ShopDTO shopDTO, User user);
    ShopDTO getShopById(UUID id);
    List<ShopDTO> getAllShops();
    ShopDTO updateShop(UUID id, ShopDTO shopDTO);
    void deleteShop(UUID id);
}
