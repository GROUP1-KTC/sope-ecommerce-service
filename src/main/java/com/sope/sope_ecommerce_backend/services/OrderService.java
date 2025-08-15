package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.OrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;

import java.util.UUID;

public interface OrderService {
    public OrderResponse createOrder(OrderCreateRequest request);

    public Iterable<OrderResponse> getAllOrders();

    public Iterable<OrderResponse> getOrdersByUserId(UUID userId);

    public OrderResponse getOrderById(UUID id);

    public OrderResponse cancelOrder(UUID id);

    public OrderResponse updateOrder(UUID id, OrderCreateRequest request);
}
