package com.sope.sope_ecommerce_backend.event;

import com.sope.sope_ecommerce_backend.dto.request.OrderEmailRequest;
import com.sope.sope_ecommerce_backend.dto.response.PaymentResponse;

import java.util.List;

public record PaymentCreatedEvent(List<OrderEmailRequest> ordersForEmail, PaymentResponse paymentResponse) {
}