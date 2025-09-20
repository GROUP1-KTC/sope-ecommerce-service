package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email
) {}
