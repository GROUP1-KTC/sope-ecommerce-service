package com.sope.sope_ecommerce_backend.dto.request;

import java.util.List;
import java.util.UUID;

public record UserLoginResponseFE(
        UUID id,
        String username,
        List<String> roles,
        String accessToken,
        String tempToken,
        boolean twoFaRequired,
        String faceAuthId
) {
}
