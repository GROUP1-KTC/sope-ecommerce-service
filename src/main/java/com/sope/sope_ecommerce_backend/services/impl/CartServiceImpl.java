package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.response.CartItemResponseDTO;
import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.repositories.*;
import com.sope.sope_ecommerce_backend.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor

public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;



    @Override
    public void addToCart(UUID userId, UUID productVariantId, int quantity) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProductVariantEntity product = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartEntity cart = cartRepository.findByUser(user).orElseGet(() -> {
            CartEntity newCart = new CartEntity();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        CartItemEntity item = cartItemRepository.findByCartAndProductVariant(cart, product)
                .orElseGet(() -> {
                    CartItemEntity newItem = new CartItemEntity();
                    newItem.setCart(cart);
//                    newItem.setProductVariant(product);
                    newItem.setQuantity(0);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + quantity);
        cart.getItems().add(item);
        cartRepository.save(cart);
    }


    @Override
    public void removeItemFromCart(UUID userId, Long cartItemId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItemEntity item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cart.getItems().contains(item)) {
            throw new RuntimeException("Item not found in cart");
        }

        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        cartRepository.save(cart);
    }

    @Override
    public List<CartItemResponseDTO> getCartItemsByUser(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Chuyển đổi CartItemEntity sang CartItemResponseDTO
        List<CartItemResponseDTO> cartItems = cart.getItems().stream()
                .map(item -> new CartItemResponseDTO(
                        item.getId(),
                        item.getProductVariant(),
                        item.getQuantity()
                        ))
                .toList();

        return cartItems;
    }

}
