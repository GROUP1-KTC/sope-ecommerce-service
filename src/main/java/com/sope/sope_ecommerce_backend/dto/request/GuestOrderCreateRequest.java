package com.sope.sope_ecommerce_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;


@JsonTypeName("guest")
@Schema(description = "Order create request for guest users")
public record GuestOrderCreateRequest(
        String idempotencyKey,
        GuestInfo guestInfo,
        List<OrderItemRequest> items,
        PaymentMethod paymentMethod,
        PaymentProvider paymentProvider,
        BigDecimal shippingCharge

        ) implements OrderCreateRequest {
}
