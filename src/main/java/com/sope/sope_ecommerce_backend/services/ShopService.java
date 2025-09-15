package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.form.ShopCreateForm;
import com.sope.sope_ecommerce_backend.dto.request.ShopCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.ShopUpdateRequest;
import com.sope.sope_ecommerce_backend.dto.response.ShopResponse;
import com.sope.sope_ecommerce_backend.dto.response.ShopSearchResult;
import com.sope.sope_ecommerce_backend.entities.Shop;
import com.sope.sope_ecommerce_backend.enums.ShopStatus;

import java.util.List;
import java.util.UUID;

public interface ShopService {

    public ShopResponse createShop(ShopCreateRequest shopCreateRequest, UUID userId);

    public ShopResponse createShop(ShopCreateForm form, UUID userId);

    public ShopResponse getShop(UUID userId);

    public List<ShopResponse> getAllShops();

    public ShopResponse getShopById(UUID shopId);

    public Shop getShopEntityById(UUID shopId);

    public ShopResponse updateShop(ShopUpdateRequest shopUpdateRequest, UUID userId);

    public ShopResponse changeShopStatus(UUID shopId, Shop.Status shopStatus);

    public List<ShopSearchResult> searchShopsByName(String name);

    UUID getShopId();

}
