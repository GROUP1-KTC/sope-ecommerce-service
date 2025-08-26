package com.sope.sope_ecommerce_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @Email String email,
        @NotBlank String otp,
        @NotBlank String newPassword
) {
}
