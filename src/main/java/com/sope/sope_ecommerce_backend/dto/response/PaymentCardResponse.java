package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.enums.CardType;

import java.time.LocalDate;
import java.util.UUID;

public record PaymentCardResponse(
        UUID id,
        String cardHolderName,
        String last4Digits,
        CardType cardType,
        LocalDate expiryDate,
        Boolean isDefault
) {}

