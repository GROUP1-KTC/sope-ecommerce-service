package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.response.CartGroupResponse;
import com.sope.sope_ecommerce_backend.dto.response.CartItemResponse;
import com.sope.sope_ecommerce_backend.dto.response.ShopAddressResponse;
import com.sope.sope_ecommerce_backend.dto.response.ShopInfo;
import com.sope.sope_ecommerce_backend.entities.CartItem;
import com.sope.sope_ecommerce_backend.entities.Shop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "productVariant.product.name", target = "productName")
    @Mapping(source = "productVariant.productVariantId", target = "productVariantId")
    @Mapping(source = "productVariant.price", target = "price")
    CartItemResponse toCartItemResponseDTO(CartItem entity);

    List<CartItemResponse> toCartItemResponseDTOs(List<CartItem> entities);




    default CartGroupResponse toCartGroupResponseDTO(Shop shop, List<CartItem> items) {
        return new CartGroupResponse(
                ShopInfo.builder()
                        .id(shop.getId())
                        .name(shop.getName())
                        .avatarUrl(shop.getLogoUrl())
                        .address(
                                ShopAddressResponse.builder(
                                        ).id(shop.getAddress().getId()
                                        ).street(shop.getAddress().getStreet()
                                        ).ward(shop.getAddress().getWard()
                                        ).district(shop.getAddress().getDistrict()
                                        ).city(shop.getAddress().getCity()
                                        ).country(shop.getAddress().getCountry(
                                )).build()
                        )
                        .build(),
                toCartItemResponseDTOs(items)
        );
    }


    @Mapping(target = "id", ignore = true)
    CartItemResponse toCartItemResponseDTO(String productVariantId, int quantity, String productName);
}
