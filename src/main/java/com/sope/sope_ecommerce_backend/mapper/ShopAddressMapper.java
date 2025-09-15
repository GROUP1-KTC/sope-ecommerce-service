package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.ShopAddressRequest;
import com.sope.sope_ecommerce_backend.dto.response.ShopAddressResponse;
import com.sope.sope_ecommerce_backend.entities.ShopAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ShopAddressMapper {

    ShopAddressMapper INSTANCE = Mappers.getMapper(ShopAddressMapper.class);

    ShopAddressResponse toResponse(ShopAddress entity);

    ShopAddress toEntity(ShopAddressRequest request);

    void updateEntityFromDto(ShopAddressRequest request, @MappingTarget ShopAddress entity);
}
