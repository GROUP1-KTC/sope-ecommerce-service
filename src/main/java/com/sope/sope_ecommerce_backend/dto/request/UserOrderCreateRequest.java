package com.sope.sope_ecommerce_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@JsonTypeName("user")
@Schema(description = "Order create request for logged-in users")
public record UserOrderCreateRequest(
        UUID shippingAddressId,
        String idempotencyKey,
        List<ShopOrderRequest> shopOrders,

        PaymentMethod paymentMethod,
        PaymentProvider paymentProvider,
        Boolean isOrderedFromCart
) implements OrderCreateRequest {
}
