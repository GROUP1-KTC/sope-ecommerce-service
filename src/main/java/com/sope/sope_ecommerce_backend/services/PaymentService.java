package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.PaymentRequest;
import com.sope.sope_ecommerce_backend.dto.response.PaymentResponse;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;

import java.util.Optional;
import java.util.UUID;

public interface PaymentService {
    public PaymentResponse initiatePayment(PaymentRequest request, UUID userId);

    public void handlePaymentCallback(String providerStr, String providerPaymentId, String callbackStatus, String otherParamsJsonOrQuery);

}
