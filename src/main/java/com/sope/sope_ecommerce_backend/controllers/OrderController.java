package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.ApiResponse;
import com.sope.sope_ecommerce_backend.dto.request.OrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.CartItemResponseDTO;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.security.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.OrderService;
import com.sope.sope_ecommerce_backend.utils.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Retrieves the cart items for the currently logged-in user.
     *
     * @param currentUser the currently authenticated user
     * @return a response containing the list of cart items
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<OrderResponse>>> getAllOrders(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            if (currentUser == null) {
                return ApiResponseUtil.unauthorized("User is not logged in");
            }

            Iterable<OrderResponse> orders = orderService.getAllOrders();
            return ApiResponseUtil.success(orders, "Order fetched successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch cart items", List.of(e.getMessage()));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<Iterable<OrderResponse>>> getAllOrdersByUserAccount(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            if (currentUser == null) {
                return ApiResponseUtil.unauthorized("User is not logged in");
            }

            Iterable<OrderResponse> orders = orderService.getOrdersByUserId(currentUser.getUserId());
            return ApiResponseUtil.success(orders, "Orders fetched successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch orders", List.of(e.getMessage()));
        }
    }


    @PostMapping()
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @RequestBody OrderCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            UUID userId = (currentUser != null) ? currentUser.getUserId() : null;
            OrderResponse order = orderService.createOrder(request, userId);
            return ApiResponseUtil.created(order, "Create order successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch orders", List.of(e.getMessage()));
        }
    }
}
