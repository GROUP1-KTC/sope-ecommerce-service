package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.OrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.UpdateOrderStatusRequest;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.entities.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import org.springframework.data.domain.Page;

public interface OrderService {
    public List<? extends OrderResponse> createOrder(OrderCreateRequest request, UUID userId);

    public List<? extends OrderResponse> getAllOrders();

    public Page<? extends OrderResponse> getAllOrdersByShop(UUID shopId, int page, int size);

    public Page<? extends OrderResponse> getPendingOrdersByShop(UUID shopId, int page, int size);

    public Page<OrderResponse>getOrdersForShipper(OrderStatus status, int page, int size);

    public List<? extends OrderResponse> getRevenueByShop(UUID shopId);

    public OrderResponse getOrderByOrderNumber(String orderNumber);

    public List<? extends OrderResponse> getOrdersByUserId(UUID userId);

    public OrderResponse getOrderById(UUID id);

    public void cancelOrder(UUID id, String reason, UUID userId);

    public OrderResponse updateOrder(UUID id, OrderCreateRequest request);

    public void updateOrderStatus(UpdateOrderStatusRequest request);

    List<Order> findOrderByStatusPendingAndExpireAtBefore(LocalDateTime now);

    void saveOrder(Order order);
}
