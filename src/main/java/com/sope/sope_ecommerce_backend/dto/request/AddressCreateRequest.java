package com.sope.sope_ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressCreateRequest(
        @NotBlank String recipientName,
        @NotBlank String phoneNumber,
        @NotBlank String street,
        @NotBlank String ward,
        @NotBlank String district,
        @NotBlank String city,
        @NotBlank String country,
        @NotNull Boolean isDefault
) {}
