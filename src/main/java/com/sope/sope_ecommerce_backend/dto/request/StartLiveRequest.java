package com.sope.sope_ecommerce_backend.dto.request;

import java.util.UUID;

public record StartLiveRequest(
        UUID shopId,
        String title,
        String description,
        String thumbnail
) {
}
