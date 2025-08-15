package com.sope.sope_ecommerce_backend.services.gateways;

import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentGateway {
    PaymentProvider getProvider();
    String[] createPaymentIntent(BigDecimal amount, String idempotencyKey);
    PaymentStatus mapStatus(String callbackStatus);
    boolean verifyCallback(Map<String, String> flatParams);
}
