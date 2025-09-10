package com.sope.sope_ecommerce_backend.dto.response;

import java.util.List;

public record CartGroupResponse(
        ShopInfo shop,
        List<CartItemResponse> items
) {
}
