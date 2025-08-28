package com.sope.sope_ecommerce_backend.client;

import com.sope.sope_ecommerce_backend.dto.request.CreateMomoRequest;
import com.sope.sope_ecommerce_backend.dto.response.CreateMomoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "vnpay", url = "${payment-gateway.vnpay.pay-url:https://test.payment.momo.vn/v2/gateway/api}")
public interface VnPayApi {
    @PostMapping("/create")
    CreateMomoResponse createPaymentIntent(@RequestBody CreateMomoRequest request);
}
