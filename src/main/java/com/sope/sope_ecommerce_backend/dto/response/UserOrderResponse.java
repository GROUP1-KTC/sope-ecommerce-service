package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record UserOrderResponse(
        UUID paymentId,
        AddressResponse shippingAddress,
        ShopOrderResponse order

) implements OrderResponse {
}
