package com.sope.sope_ecommerce_backend.dto.response;

import lombok.Builder;

@Builder
public record CreateMomoResponse(
        String partnerCode,
        String orderId,
        String requestId,
        String amount,
        String responseTime,
        String message,
        String resultCode,
        String payUrl,
        String deeplink,
        String qrCodeUrl
) implements PaymentResponse{
    @Override
    public String getPayUrl() {
        return payUrl;
    }

    @Override
    public String getPaymentId() {
        return orderId;
    }
}
