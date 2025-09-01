package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.enums.Gender;

import java.time.LocalDate;

public record UserUpdateRecord(
        String name,
        Gender gender,
        LocalDate birthday,
        String avatarUrl
) {
}
