package com.sope.sope_ecommerce_backend.dto.response;

import lombok.Builder;

public interface PaymentResponse {
    String getPayUrl();
    String getPaymentId();
}
