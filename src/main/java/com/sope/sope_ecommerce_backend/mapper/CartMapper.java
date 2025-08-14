package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.response.CartItemResponseDTO;
import com.sope.sope_ecommerce_backend.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "productVariant.product.name", target = "productName")
    @Mapping(source = "productVariant.productVariantId", target = "productVariantId")
    @Mapping(source = "productVariant.price", target = "price")
    @Mapping(source = "productVariant.imageVariant", target = "imageVariant")
    CartItemResponseDTO toCartItemResponseDTO(CartItem entity);

    List<CartItemResponseDTO> toCartItemResponseDTOs(List<CartItem> entities);

    @Mapping(target = "id", ignore = true)
    CartItemResponseDTO toCartItemResponseDTO(String productVariantId, int quantity, String productName);
}
