package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.OrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.UpdateOrderStatusRequest;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.entities.Order;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    public OrderResponse createOrder(OrderCreateRequest request, UUID userId);

    public Iterable<OrderResponse> getAllOrders();

    public Iterable<OrderResponse> getOrdersByUserId(UUID userId);

    public OrderResponse getOrderById(UUID id);

    public void cancelOrder(UUID id, String reason, UUID userId);

    public OrderResponse updateOrder(UUID id, OrderCreateRequest request);

    public void updateOrderStatus(UpdateOrderStatusRequest request);


    List<Order> findOrderByStatusPendingAndExpireAtBefore(LocalDateTime now);
}
