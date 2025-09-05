package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.enums.UserStatus;

import java.util.UUID;

public record UserInformationResponse(
        UUID id,
        String username,
        String phone,
        UserStatus status,
        String name,
        String email,
        String gender,
        String address
) {}
