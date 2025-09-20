package com.sope.sope_ecommerce_backend.client;

import com.sope.sope_ecommerce_backend.dto.request.CreateMomoRequest;
import com.sope.sope_ecommerce_backend.dto.response.CreateMomoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "momo", url = "${payment-gateway.momo.endpoint:https://test.payment.momo.vn/v2/gateway/api}")
public interface MomoApi {
    @PostMapping("/create")
    CreateMomoResponse createPaymentIntent(@RequestBody CreateMomoRequest request);
}
