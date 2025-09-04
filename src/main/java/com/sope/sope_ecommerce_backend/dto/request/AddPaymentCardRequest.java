package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.enums.CardType;

import java.time.LocalDate;

public record AddPaymentCardRequest(
        String cardNumber,
        String cardHolderName,
        CardType cardType,
        LocalDate expiryDate
) {}

