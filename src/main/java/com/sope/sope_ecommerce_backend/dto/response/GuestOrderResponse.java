package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GuestOrderResponse(
        UUID id,
        String orderNumber,
        String guestName,
        String guestPhone,
        String guestEmail,
        String shippingAddress,
        String city,
        String district,
        String ward,
        BigDecimal subtotal,
        BigDecimal shippingCharges,
        BigDecimal totalAmount,
       PaymentMethod paymentMethod,
        PaymentProvider paymentProvider,
        PaymentStatus paymentStatus,
        LocalDateTime createdAt,
        List<OrderItemResponse> orderItems
) implements OrderResponse {
}
