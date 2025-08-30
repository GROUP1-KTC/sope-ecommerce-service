package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.AddToCartRequestDTO;
import com.sope.sope_ecommerce_backend.dto.request.UpdateCartItemRequestDTO;
import com.sope.sope_ecommerce_backend.dto.response.CartGroupResponse;
import com.sope.sope_ecommerce_backend.dto.response.CartItemResponse;
import com.sope.sope_ecommerce_backend.entities.Cart;

import java.util.List;
import java.util.UUID;

public interface CartService {
    void addToCart(UUID userId, UUID productVariantId, int quantity);
    List<CartGroupResponse> getCartByUser(UUID userId);
    void removeItemFromCart(UUID userId, UUID cartItemId);

    void updateCartItem(UUID userId, UUID cartItemId, UpdateCartItemRequestDTO request);

    List<CartItemResponse> validateGuestCart(List<AddToCartRequestDTO> items);

    void removeItemsFromCart(UUID userId, List<UUID> productVariantIds);

    Cart getCartEntityByUser(UUID userId);
}

