package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.ApiResponse;
import com.sope.sope_ecommerce_backend.dto.request.AddToCartRequestDTO;
import com.sope.sope_ecommerce_backend.dto.request.UpdateCartItemRequestDTO;
import com.sope.sope_ecommerce_backend.dto.response.CartItemResponseDTO;
import com.sope.sope_ecommerce_backend.security.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.CartService;
import com.sope.sope_ecommerce_backend.utils.ApiResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    // ===== USER LOGIN =====

    /**
     * Adds an item to the user's cart.
     *
     * @param request the request containing product variant ID and quantity
     * @return a response indicating success or failure
     */
    @PostMapping()
    public ResponseEntity<ApiResponse<Object>> addToCart(@RequestBody AddToCartRequestDTO request,
         @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        try {
            if (currentUser == null) {
                return ApiResponseUtil.unauthorized("User is not logged in");
            }

            if ( request.productVariantId() == null || request.quantity() <= 0) {
                return ApiResponseUtil.badRequest(List.of("Invalid input: userId, productVariantId, or quantity"));
            }

            cartService.addToCart(currentUser.getUserId(), request.productVariantId(), request.quantity());
            return ApiResponseUtil.success(null, "Added to cart successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to add item to cart", List.of(e.getMessage()));
        }
    }

    /**
     * Retrieves the items in the user's cart.
     *
     * @return a response containing the list of cart items
     */
    @GetMapping()
    public ResponseEntity<ApiResponse<List<CartItemResponseDTO>>> getCart(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            if (currentUser == null) {
                return ApiResponseUtil.unauthorized("User is not logged in");
            }

            List<CartItemResponseDTO> items = cartService.getCartByUser(currentUser.getUserId());
            return ApiResponseUtil.success(items, "Cart fetched successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch cart items", List.of(e.getMessage()));
        }
    }

    /**
     * Updates the quantity of an item in the user's cart.
     *
     * @param itemId  the ID of the cart item to update
     * @param request the request containing the new quantity
     * @return a response indicating success or failure
     */

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<Object>> updateCartItem(
            @PathVariable Long itemId,
            @RequestBody UpdateCartItemRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        try {
            if (currentUser == null) {
                return ApiResponseUtil.unauthorized("User is not logged in");
            }

            UUID userId = currentUser.getUserId();
            if (userId == null) {
                return ApiResponseUtil.badRequest(List.of("User ID must not be null"));
            }

            cartService.updateCartItem(userId, itemId, request);
            return ApiResponseUtil.success(null, "Cart item updated successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to update cart item", List.of(e.getMessage()));
        }
    }

    /**
     * Removes an item from the user's cart.
     *
     * @param cartItemId the ID of the cart item to remove
     * @return a response indicating success or failure
     */
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse<Object>> removeItem(@PathVariable Long cartItemId,
                                                          @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            if (currentUser == null) {
                return ApiResponseUtil.unauthorized("User is not logged in");
            }

            if (cartItemId == null) {
                return ApiResponseUtil.badRequest(List.of("cartItemId and userId must not be null"));
            }

            cartService.removeItemFromCart(currentUser.getUserId(), cartItemId);
            return ApiResponseUtil.success(null, "Item removed from cart.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to remove cart item", List.of(e.getMessage()));
        }
    }


    // ===== GUEST =====
    /**
     * Validates the guest cart items.
     *
     * @param items the list of items to validate
     * @return a response containing the validated cart items
     */
    @PostMapping("/guest/validate")
    public ResponseEntity<ApiResponse<List<CartItemResponseDTO>>> validateGuestCart(@RequestBody List<AddToCartRequestDTO> items) {
        List<CartItemResponseDTO> validatedItems = cartService.validateGuestCart(items);
        return ApiResponseUtil.success(validatedItems, "Guest cart validated successfully.");
    }
}
