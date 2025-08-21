package com.sope.sope_ecommerce_backend.services.gateways;

import com.sope.sope_ecommerce_backend.dto.request.PaymentRequest;
import com.sope.sope_ecommerce_backend.dto.response.PaymentResponse;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentGateway <T extends PaymentResponse> {
    PaymentProvider getProvider();
    T createPaymentIntent(PaymentRequest request);
    PaymentStatus mapStatus(String callbackStatus);
    boolean verifyCallback(Map<String, String> flatParams);
}
