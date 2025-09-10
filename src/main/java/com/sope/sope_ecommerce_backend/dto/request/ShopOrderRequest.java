package com.sope.sope_ecommerce_backend.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ShopOrderRequest(
        UUID shopId,
        BigDecimal shippingCharge,
        String note,
        List<String> discountCodes,
        List<OrderItemRequest> items,
        String shippingRateId

) {
}
