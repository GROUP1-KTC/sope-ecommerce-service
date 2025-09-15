package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.ApiResponse;
import com.sope.sope_ecommerce_backend.dto.request.CancelOrderRequest;
import com.sope.sope_ecommerce_backend.dto.request.OrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.UpdateOrderStatusRequest;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.enums.RoleName;
import com.sope.sope_ecommerce_backend.security.user.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.OrderService;
import com.sope.sope_ecommerce_backend.utils.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

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
    public ResponseEntity<ApiResponse<List<? extends OrderResponse>>> getAllOrders(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            if (currentUser == null) {
                return ApiResponseUtil.unauthorized("User is not logged in");
            }

            List<? extends OrderResponse> orders = orderService.getAllOrders();
            return ApiResponseUtil.success(orders, "Order fetched successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch cart items", List.of(e.getMessage()));
        }
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByOrderNumber(
            @PathVariable String orderNumber) {
        try {
            OrderResponse order = orderService.getOrderByOrderNumber(orderNumber);
            return ApiResponseUtil.success(order, "Order fetched successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch order", List.of(e.getMessage()));
        }
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<ApiResponse<Page<? extends OrderResponse>>> getAllOrdersByShop(
            @PathVariable UUID shopId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Page<? extends OrderResponse> orders = orderService.getAllOrdersByShop(shopId, page, size);
            return ApiResponseUtil.success(orders, "Orders for shop fetched successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch orders", List.of(e.getMessage()));
        }
    }

    @GetMapping("/shop/{shopId}/pending")
    public ResponseEntity<ApiResponse<Page<? extends OrderResponse>>> getPendingOrdersByShop(
            @PathVariable UUID shopId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Page<? extends OrderResponse> orders = orderService.getPendingOrdersByShop(shopId, page, size);
            return ApiResponseUtil.success(orders, "Pending orders for shop fetched successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch pending orders", List.of(e.getMessage()));
        }
    }


    @GetMapping("/shipper")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getOrdersForShipper(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            if(currentUser == null) {
                return ApiResponseUtil.unauthorized("User is not logged in");
            }

            if(!currentUser.getRoles().contains(RoleName.SHIPPER)){
                return ApiResponseUtil.forbidden("Access denied: User is not a shipper");
            }

            Page<OrderResponse> orders = orderService.getOrdersForShipper(status, page, size);
            return ApiResponseUtil.success(orders, "Pending orders for shop fetched successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch pending orders", List.of(e.getMessage()));
        }
    }


    @GetMapping("/revenue/{shopId}")
    public ResponseEntity<ApiResponse<List<? extends OrderResponse>>> getRevenueByShop(@PathVariable UUID shopId) {
        try {
            List<? extends OrderResponse> orders = orderService.getRevenueByShop(shopId);
            return ApiResponseUtil.success(orders, "Orders for shop fetched successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch orders", List.of(e.getMessage()));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<? extends OrderResponse>>> getAllOrdersByUserAccount(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            if (currentUser == null) {
                return ApiResponseUtil.unauthorized("User is not logged in");
            }

            List<? extends OrderResponse> orders = orderService.getOrdersByUserId(currentUser.getUserId());
            return ApiResponseUtil.success(orders, "Orders fetched successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch orders", List.of(e.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<List<? extends OrderResponse>>> createOrder(
            @RequestBody OrderCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            UUID userId = (currentUser != null) ? currentUser.getUserId() : null;
            List<? extends OrderResponse> order = orderService.createOrder(request, userId);
            return ApiResponseUtil.created(order, "Create order successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to create order", List.of(e.getMessage()));
        }
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse<String>> cancelOrder(@PathVariable UUID id,
            @RequestBody CancelOrderRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {

            if (currentUser == null) {
                return ApiResponseUtil.unauthorized("User is not logged in");
            }

            String cancelReason = request.reason();

            orderService.cancelOrder(id, cancelReason, currentUser.getUserId());
            return ApiResponseUtil.success(null, "Order cancelled successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to cancel order", List.of(e.getMessage()));
        }
    }

    @PatchMapping("/update-status")
    public ResponseEntity<ApiResponse<String>> updateOrderStatus(@RequestBody UpdateOrderStatusRequest request) {
        try {
            orderService.updateOrderStatus(request);
            return ApiResponseUtil.success(null, "Order status updated successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to update order status", List.of(e.getMessage()));
        }
    }
}
