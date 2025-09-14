package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.dto.request.OrderItemRequest;
import com.sope.sope_ecommerce_backend.entities.OrderStatusHistory;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record ShopOrderResponse(
                ShopInfo shopInfo,
                UUID orderId,
                String orderNumber,
                BigDecimal shippingCharges,
                BigDecimal subTotal,
                BigDecimal totalAmount,

                OrderStatus status,
                String note,
                String cancelReason,
                String shippingRateId,
                Set<OrderDiscountResponse> orderDiscounts,
                List<OrderItemResponse> items,
                PaymentMethod paymentMethod,
                PaymentProvider paymentProvider,
                PaymentStatus paymentStatus,

                String paymentPayUrl,

                List<OrderStatusHistoryResponse> statusHistory,

                LocalDateTime createdAt) {
}
