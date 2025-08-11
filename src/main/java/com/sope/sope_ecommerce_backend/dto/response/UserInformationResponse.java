package com.sope.sope_ecommerce_backend.dto.response;

public record UserInformationResponse(
        String username,
        String name,
        String email,
        String phone,
        String address
) {}
