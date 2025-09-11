package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.enums.UserStatus;

public record UserStatusRequest(
        UserStatus status,
        String note

) {
}
