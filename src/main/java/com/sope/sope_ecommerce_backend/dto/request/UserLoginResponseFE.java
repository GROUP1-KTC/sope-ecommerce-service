package com.sope.sope_ecommerce_backend.dto.request;

import java.util.List;

public record UserLoginResponseFE(
        String username,
        List<String> roles
) {
}
