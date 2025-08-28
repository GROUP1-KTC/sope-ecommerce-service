package com.sope.sope_ecommerce_backend.dto.request;

public record MomoCallBackRequest(
        String partnerCode,
        String orderId,
        String requestId,
        String amount,
        String orderInfo,
        String orderType,
        String transId,
        String resultCode,
        String message,
        String payType,
        String responseTime,
        String extraData,
        String signature
) {
}
