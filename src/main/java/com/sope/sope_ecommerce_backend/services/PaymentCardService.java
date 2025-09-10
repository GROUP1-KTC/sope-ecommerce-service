package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.AddPaymentCardRequest;
import com.sope.sope_ecommerce_backend.dto.response.PaymentCardResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentCardService {
    PaymentCardResponse addPaymentCard(UUID userId, AddPaymentCardRequest request);

    List<PaymentCardResponse> getUserPaymentCards(UUID userId);

    void deletePaymentCard(UUID cardId);

    PaymentCardResponse setDefaultCard(UUID userId, UUID cardId);
}
