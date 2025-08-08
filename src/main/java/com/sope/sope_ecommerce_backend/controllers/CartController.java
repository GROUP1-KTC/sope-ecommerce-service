package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.ApiResponse;
import com.sope.sope_ecommerce_backend.dto.request.AddToCartRequestDTO;
import com.sope.sope_ecommerce_backend.dto.response.CartItemResponseDTO;
import com.sope.sope_ecommerce_backend.services.CartService;
import com.sope.sope_ecommerce_backend.utils.ApiResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.sope.sope_ecommerce_backend.security.SecurityUtil.getCurrentUserId;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Object>> addToCart(@RequestBody AddToCartRequestDTO request) {
        try {
            if ( request.productVariantId() == null || request.quantity() <= 0) {
                return ApiResponseUtil.badRequest(List.of("Invalid input: userId, productVariantId, or quantity"));
            }

            cartService.addToCart(getCurrentUserId(), request.productVariantId(), request.quantity());
            return ApiResponseUtil.success(null, "Added to cart successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to add item to cart", List.of(e.getMessage()));
        }
    }

    @GetMapping("/user")
    public  ResponseEntity<ApiResponse<List<CartItemResponseDTO>>> getCart() {
        try {
            UUID userId = getCurrentUserId();
            if (userId == null) {
                return ApiResponseUtil.badRequest(List.of("User ID must not be null"));
            }

            List<CartItemResponseDTO> items = cartService.getCartItemsByUser(userId);
            return ApiResponseUtil.success(items, "Cart fetched successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch cart items", List.of(e.getMessage()));
        }
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<ApiResponse<Object>> removeItem(@PathVariable Long cartItemId) {
        try {
            UUID userId = getCurrentUserId();

            if (cartItemId == null || userId == null) {
                return ApiResponseUtil.badRequest(List.of("cartItemId and userId must not be null"));
            }

            cartService.removeItemFromCart(userId, cartItemId);
            return ApiResponseUtil.success(null, "Item removed from cart.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to remove cart item", List.of(e.getMessage()));
        }
    }
}
