package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;

public record ShopSearchResult(
        UUID id,
        String name,
        String logoUrl
) {}
