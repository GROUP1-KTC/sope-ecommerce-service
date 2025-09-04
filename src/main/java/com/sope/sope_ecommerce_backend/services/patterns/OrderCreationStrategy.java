package com.sope.sope_ecommerce_backend.services.patterns;

import com.sope.sope_ecommerce_backend.dto.request.OrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.entities.Order;

import java.util.List;
import java.util.UUID;

public interface OrderCreationStrategy<T extends OrderCreateRequest> {
    boolean supports(OrderCreateRequest request);
    List<? extends OrderResponse> createOrder(T request, UUID userId);
}
