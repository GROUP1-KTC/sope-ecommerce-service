package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.AddToCartRequestDTO;
import com.sope.sope_ecommerce_backend.dto.request.UpdateCartItemRequestDTO;
import com.sope.sope_ecommerce_backend.dto.response.CartGroupResponse;
import com.sope.sope_ecommerce_backend.dto.response.CartItemResponse;
import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.mapper.CartMapper;
import com.sope.sope_ecommerce_backend.repositories.*;
import com.sope.sope_ecommerce_backend.repositories.specification.CartItemSpecification;
import com.sope.sope_ecommerce_backend.services.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Transactional
    public void addToCart(UUID userId, UUID productVariantId, int quantity) {
        Optional<CartItem> existingItemOpt = cartItemRepository.findOne(
                CartItemSpecification.byUserAndVariantFetch(userId, productVariantId)
        );

        Cart cart;
        CartItem item;
        ProductVariant productVariant;

        if (existingItemOpt.isPresent()) {
            item = existingItemOpt.get();
            cart = item.getCart();
            productVariant = item.getProductVariant();
        } else {
            AppUser appUser = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            productVariant = productVariantRepository.findById(productVariantId)
                    .orElseThrow(() -> new EntityNotFoundException("ProductVariant not found"));

            cart = cartRepository.findByAppUser(appUser)
                    .orElseGet(() -> {
                        Cart newCart = new Cart();
                        newCart.setAppUser(appUser);
                        return cartRepository.save(newCart);
                    });

            item = new CartItem();
            item.setCart(cart);
            item.setProductVariant(productVariant);
            item.setQuantity(0);
        }

        // Check stock
        int newQuantity = item.getQuantity() + quantity;
        if (newQuantity > productVariant.getStock()) {
            throw new IllegalArgumentException("Quantity exceeds available stock");
        }

        item.setQuantity(newQuantity);
        cartItemRepository.save(item);
    }

    /**
     * Updates an existing cart item with a new variant or quantity.
     *
     * @param userId     the ID of the user
     * @param cartItemId the ID of the cart item to update
     * @param request    the request containing new variant ID and/or quantity
     */
    @Override
    public void updateCartItem(UUID userId, UUID cartItemId, UpdateCartItemRequestDTO request) {

        CartItem item = cartItemRepository.findOne(
                CartItemSpecification.byUserAndItemIdFetch(userId, cartItemId)
        ).orElseThrow(() -> new EntityNotFoundException("Cart item not found for this user"));


        // Đổi variant
        if (request.newVariantId() != null) {
            ProductVariant newVariant = productVariantRepository.findById(request.newVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Variant not found"));

            if (newVariant.getStock() <= 0) {
                throw new IllegalArgumentException("Selected variant is out of stock");
            }

            // Nếu cart đã có item với variant mới thì gộp quantity
            Optional<CartItem> sameVariantOpt = cartItemRepository.findOne(
                    CartItemSpecification.byUserAndVariantFetch(userId, request.newVariantId())
            );

            if (sameVariantOpt.isPresent() && !sameVariantOpt.get().getId().equals(item.getId())) {
                CartItem sameVariantItem = sameVariantOpt.get();
                int newQty = sameVariantItem.getQuantity() + item.getQuantity();
                if (newQty > newVariant.getStock()) {
                    throw new IllegalArgumentException("Total quantity exceeds available stock");
                }
                sameVariantItem.setQuantity(newQty);
                cartItemRepository.delete(item);
                cartItemRepository.save(sameVariantItem);
                return;
            }

            item.setProductVariant(newVariant);
        }

        // Đổi số lượng
        if (request.quantity() != null) {
            int availableStock = item.getProductVariant().getStock();
            if (request.quantity() > availableStock) {
                throw new IllegalArgumentException("Quantity exceeds available stock");
            }
            item.setQuantity(request.quantity());
        }

        cartItemRepository.save(item);
    }


    /**
     * Removes multiple items from the user's cart.
     *
     * @param userId      the ID of the user
     * @param productVariantIds the list of product variant IDs to remove
     */
    @Override
    @Transactional
    public void removeItemsFromCart(UUID userId, List<UUID> productVariantIds) {
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByAppUser(appUser)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> items = cart.getItems();
        if (items.isEmpty()) {
            return;
        }

        List<CartItem> itemsToRemove = items.stream()
                .filter(item -> productVariantIds.contains(item.getProductVariant().getProductVariantId()))
                .toList();

        if (!itemsToRemove.isEmpty()) {
            cart.getItems().removeAll(itemsToRemove);
            cartItemRepository.deleteAll(itemsToRemove);
            cartRepository.save(cart);
        }
    }


    /**
     * Removes an item from the user's cart.
     *
     * @param userId     the ID of the user
     * @param cartItemId the ID of the cart item to remove
     */
    @Override
    public void removeItemFromCart(UUID userId, UUID cartItemId) {
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByAppUser(appUser)
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
    public  List<CartGroupResponse> getCartByUser(UUID userId) {
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByAppUser(appUser)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Map<Shop, List<CartItem>> groupedByShop = cart.getItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProductVariant().getProduct().getShop()));



        return groupedByShop.entrySet().stream()
                .map(entry -> cartMapper.toCartGroupResponseDTO(entry.getKey(), entry.getValue()))
                .toList();
    }


    @Override
    public Cart getCartEntityByUser(UUID userId) {
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByAppUser(appUser)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        return cart;
    }

    /**
     * Validates the items in a guest cart, ensuring that each item has sufficient
     * stock.
     *
     * @param items the list of items to validate
     * @return a list of CartItemResponseDTO with validated quantities
     */
    @Override
    public List<CartItemResponse> validateGuestCart(List<AddToCartRequestDTO> items) {
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
