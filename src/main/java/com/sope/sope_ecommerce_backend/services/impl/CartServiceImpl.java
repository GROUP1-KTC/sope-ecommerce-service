package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.AddToCartRequestDTO;
import com.sope.sope_ecommerce_backend.dto.request.UpdateCartItemRequestDTO;
import com.sope.sope_ecommerce_backend.dto.response.CartItemResponseDTO;
import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.mapper.CartMapper;
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
    private final CartMapper cartMapper;

    /**
     * Adds an item to the user's cart. If the cart does not exist, it creates a new
     * one.
     * If the item already exists in the cart, it updates the quantity.
     *
     * @param userId           the ID of the user
     * @param productVariantId the ID of the product variant to add
     * @param quantity         the quantity of the product variant to add
     */
    @Override
    public void addToCart(UUID userId, UUID productVariantId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new RuntimeException("ProductVariant not found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        CartItem item = cartItemRepository.findByCartAndProductVariant(cart, productVariant)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProductVariant(productVariant);
                    newItem.setQuantity(0);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + quantity);
        cart.getItems().add(item);
        cartRepository.save(cart);
    }

    /**
     * Updates an existing cart item with a new variant or quantity.
     *
     * @param userId     the ID of the user
     * @param cartItemId the ID of the cart item to update
     * @param request    the request containing new variant ID and/or quantity
     */
    @Override
    public void updateCartItem(UUID userId, Long cartItemId, UpdateCartItemRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cart.getItems().contains(item)) {
            throw new RuntimeException("Item not found in cart");
        }

        // Change varriant
        if (request.newVariantId() != null) {
            ProductVariant newVariant = productVariantRepository.findById(request.newVariantId())
                    .orElseThrow(() -> new RuntimeException("Variant not found"));

            if (newVariant.getStock() <= 0) {
                throw new RuntimeException("Selected variant is out of stock");
            }

            item.setProductVariant(newVariant);
        }

        // change quantity
        if (request.quantity() != null) {
            int availableStock = item.getProductVariant().getStock();
            if (request.quantity() <= 0) {
                throw new RuntimeException("Quantity must be greater than 0");
            }
            if (request.quantity() > availableStock) {
                throw new RuntimeException("Quantity exceeds available stock");
            }
            item.setQuantity(request.quantity());
        }

        cartItemRepository.save(item);
    }

    /**
     * Removes an item from the user's cart.
     *
     * @param userId     the ID of the user
     * @param cartItemId the ID of the cart item to remove
     */
    @Override
    public void removeItemFromCart(UUID userId, Long cartItemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cart.getItems().contains(item)) {
            throw new RuntimeException("Item not found in cart");
        }

        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        cartRepository.save(cart);
    }

    /**
     * Retrieves the items in the user's cart.
     *
     * @param userId the ID of the user
     * @return a list of CartItemResponseDTO representing the items in the cart
     */
    @Override
    public List<CartItemResponseDTO> getCartByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        return cartMapper.toCartItemResponseDTOs(cart.getItems());
    }

    /**
     * Validates the items in a guest cart, ensuring that each item has sufficient
     * stock.
     *
     * @param items the list of items to validate
     * @return a list of CartItemResponseDTO with validated quantities
     */
    @Override
    public List<CartItemResponseDTO> validateGuestCart(List<AddToCartRequestDTO> items) {
        // return items.stream().map(item -> {
        // ProductVariant product =
        // productVariantRepository.findById(item.productVariantId())
        // .orElseThrow(() -> new RuntimeException("Product not found"));
        // return
        // validateItem(cartMapper.toCartItemResponseDTO(product.getProductVariantId(),
        // item.quantity()));
        // }).toList();
        return null;
    }

    /**
     * Validates a single cart item, ensuring that the quantity does not exceed
     * available stock.
     *
     * @param dto the CartItemResponseDTO to validate
     * @return a validated CartItemResponseDTO with adjusted quantity if necessary
     */
    // private CartItemResponseDTO validateItem(CartItemResponseDTO dto) {
    // // Check tồn kho
    // int availableStock = dto.productVariant().getStock();
    // int quantity = Math.min(dto.quantity(), availableStock);

    // return new CartItemResponseDTO(dto.id(), dto.productName(),
    // dto.productVariant(), quantity);
    // }

}
