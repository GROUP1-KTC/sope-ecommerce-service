package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.enums.IdentifierType;

public record ShopIdentificationRequest(
        IdentifierType idType,
        String idName,
        String idNumber,
        String idFront,
        String idBack,
        String selfie
) {}
