
package com.sope.sope_ecommerce_backend.dto.response;

import lombok.Builder;

import java.util.UUID;


@Builder
public record ShopInfo(
            UUID id,
            String name,
            String avatarUrl,
            ShopAddressResponse address
) {
}