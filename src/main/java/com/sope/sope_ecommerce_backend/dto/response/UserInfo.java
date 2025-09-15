package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;

public record UserInfo(
            UUID id,
            String username,
            String name) {
}
