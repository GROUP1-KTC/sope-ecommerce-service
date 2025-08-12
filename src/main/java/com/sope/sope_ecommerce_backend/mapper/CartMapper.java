package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.response.CartItemResponseDTO;
import com.sope.sope_ecommerce_backend.entities.CartItem;
import com.sope.sope_ecommerce_backend.entities.ProductVariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "productVariant.product.name", target = "productName")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "quantity", target = "quantity")
    CartItemResponseDTO toCartItemResponseDTO(CartItem entity);

    List<CartItemResponseDTO> toCartItemResponseDTOs(List<CartItem> entities);

    @Mapping(source = "productVariant.product.name", target = "productName")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "quantity", target = "quantity")
    CartItemResponseDTO toCartItemResponseDTO(ProductVariantEntity productVariant, int quantity);
}
