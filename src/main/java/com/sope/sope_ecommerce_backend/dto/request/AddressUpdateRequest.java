package com.sope.sope_ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressUpdateRequest(
        @NotBlank String recipientName,
        @NotBlank String phoneNumber,
        @NotBlank String street,
        @NotBlank String ward,
        @NotBlank String district,
        @NotBlank String city,
        @NotBlank String country,
        Boolean isDefault
) {}
