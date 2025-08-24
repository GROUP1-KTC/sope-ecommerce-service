package com.sope.sope_ecommerce_backend.controllers;


import com.sope.sope_ecommerce_backend.constant.MomoVariable;
import com.sope.sope_ecommerce_backend.dto.request.PaymentRequest;
import com.sope.sope_ecommerce_backend.dto.response.PaymentResponse;
import com.sope.sope_ecommerce_backend.exception.CustomException;
import com.sope.sope_ecommerce_backend.security.user.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.PaymentService;
import com.sope.sope_ecommerce_backend.utils.ParamUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(@RequestBody PaymentRequest request,
                                                            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        UUID userId = (currentUser != null) ? currentUser.getUserId() : null;
        PaymentResponse response = paymentService.initiatePayment(request, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook/momo")
    public ResponseEntity<String> momoIpn(@RequestBody Map<String, String> request) {
        Map<String, String> flat = new HashMap<>();
        request.forEach((k,v) -> flat.put(k, v == null ? "" : v.toString()));
        String providerPaymentId = flat.get("orderId");
        String providerStatus = flat.get(MomoVariable.RESULT_CODE);
        paymentService.handlePaymentCallback("MOMO", providerPaymentId, providerStatus, ParamUtil.toQueryString(flat, true));
        return ResponseEntity.ok(providerStatus.equals("0") ? "Payment success" : "Payment failed");
    }

    @RequestMapping(value="/webhook/vnpay", method={RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> vnpayIpn(HttpServletRequest req) {
        Map<String, String> flat = ParamUtil.flatten(req.getParameterMap());
        String providerPaymentId = flat.get("vnp_TxnRef");
        String providerStatus = flat.get("vnp_ResponseCode"); // "00" success
        paymentService.handlePaymentCallback("VNPAY", providerPaymentId, providerStatus, ParamUtil.toQueryString(flat, true));
        return ResponseEntity.ok("OK");
    }

//    @GetMapping("/payment/resume")
//    public PaymentInfo resumePayment(@RequestParam String token) {
//        TempOrder tempOrder = tempOrderRepository.findByResumeToken(token)
//                .orElseThrow(() -> new CustomException("Order not found or expired"));
//
//        // trả về thông tin order, tổng tiền, items, etc.
//        return mapToPaymentInfo(tempOrder);
//    }
}
