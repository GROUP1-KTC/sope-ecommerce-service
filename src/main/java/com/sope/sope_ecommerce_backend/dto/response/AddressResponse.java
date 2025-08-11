package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;

public record AddressResponse(
        UUID id,
        String recipientName,
        String phoneNumber,
        String street,
        String ward,
        String district,
        String city,
        String country,
        boolean isDefault
) {
}
