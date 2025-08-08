package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.response.CartItemResponseDTO;

import java.util.List;
import java.util.UUID;

// Interface
public interface CartService {
    void addToCart(UUID userId, UUID productVariantId, int quantity);
    List<CartItemResponseDTO> getCartItemsByUser(UUID userId);
    void removeItemFromCart(UUID userId, Long cartItemId);
}

