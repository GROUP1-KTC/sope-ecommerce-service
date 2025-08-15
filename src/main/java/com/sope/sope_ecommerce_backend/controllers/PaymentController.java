package com.sope.sope_ecommerce_backend.controllers;


import com.sope.sope_ecommerce_backend.dto.request.PaymentRequest;
import com.sope.sope_ecommerce_backend.dto.response.PaymentResponse;
import com.sope.sope_ecommerce_backend.services.PaymentService;
import com.sope.sope_ecommerce_backend.utils.ParamUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.initiatePayment(request);
        return ResponseEntity.ok(response); // Client redirect to response.providerPayUrl
    }

    // Webhook endpoint (public, nhưng secure bằng signature)
    @PostMapping("/callback/{provider}")
    public ResponseEntity<Void> handleCallback(
            @PathVariable String provider,
            @RequestParam String providerPaymentId,
            @RequestParam String callbackStatus,
            @RequestParam(required = false) String otherParams) {
        paymentService.handlePaymentCallback(provider, providerPaymentId, callbackStatus, otherParams);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/webhook/momo")
    public ResponseEntity<String> momoIpn(@RequestBody Map<String, Object> body) {
        Map<String, String> flat = new HashMap<>();
        body.forEach((k,v) -> flat.put(k, v == null ? "" : v.toString()));
        String providerPaymentId = flat.get("orderId");
        String providerStatus = flat.get("resultCode"); // "0" success
        paymentService.handlePaymentCallback("MOMO", providerPaymentId, providerStatus, /* serialize to query/json */ ParamUtil.toQueryString(flat, true));
        return ResponseEntity.ok("0");
    }

    @RequestMapping(value="/webhook/vnpay", method={RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> vnpayIpn(HttpServletRequest req) {
        Map<String, String> flat = ParamUtil.flatten(req.getParameterMap());
        String providerPaymentId = flat.get("vnp_TxnRef");
        String providerStatus = flat.get("vnp_ResponseCode"); // "00" success
        paymentService.handlePaymentCallback("VNPAY", providerPaymentId, providerStatus, ParamUtil.toQueryString(flat, true));
        return ResponseEntity.ok("OK");
    }
}
