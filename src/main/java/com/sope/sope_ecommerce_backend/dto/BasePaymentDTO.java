package com.sope.sope_ecommerce_backend.dto;

import com.sope.sope_ecommerce_backend.dto.response.PaymentResponse;
import com.sope.sope_ecommerce_backend.entities.TempOrder;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;

public record BasePaymentDTO(
        String payUrl,
        String paymentId,
        PaymentMethod paymentMethod,
        PaymentProvider provider

) implements PaymentResponse {
    @Override
    public String getPayUrl() {
        return payUrl;
    }

    @Override
    public String getPaymentId() {
        return paymentId;
    }
}