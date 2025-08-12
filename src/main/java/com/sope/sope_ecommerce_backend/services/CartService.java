package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.AddToCartRequestDTO;
import com.sope.sope_ecommerce_backend.dto.request.UpdateCartItemRequestDTO;
import com.sope.sope_ecommerce_backend.dto.response.CartItemResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CartService {
    void addToCart(UUID userId, UUID productVariantId, int quantity);
    List<CartItemResponseDTO> getCartByUser(UUID userId);
    void removeItemFromCart(UUID userId, Long cartItemId);

    void updateCartItem(UUID userId, Long cartItemId, UpdateCartItemRequestDTO request);

    List<CartItemResponseDTO> validateGuestCart(List<AddToCartRequestDTO> items);
}

