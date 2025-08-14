package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.ShopCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.ShopUpdateRequest;
import com.sope.sope_ecommerce_backend.dto.response.ShopResponse;
import com.sope.sope_ecommerce_backend.entities.Shop;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ShopMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appUser", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Shop toEntity(ShopCreateRequest request);

    ShopResponse toResponse(Shop shop);

    @Mapping(target = "appUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateShopFromDTO(ShopUpdateRequest dto, @MappingTarget Shop shop);
}
