package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.dto.request.GuestInfo;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GuestOrderResponse(
        GuestInfo guestInfo,
        ShopOrderResponse order
) implements OrderResponse {
}
