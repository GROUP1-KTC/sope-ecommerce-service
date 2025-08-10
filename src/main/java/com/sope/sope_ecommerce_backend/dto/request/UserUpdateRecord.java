package com.sope.sope_ecommerce_backend.dto.request;

public record UserUpdateRecord(
        String username,
        String name,
        String email,
        String phone,
        String address,
        String note,
        String status
) {
}
