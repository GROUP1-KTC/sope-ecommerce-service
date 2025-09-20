package com.sope.sope_ecommerce_backend.services.gateways;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sope.sope_ecommerce_backend.client.MomoApi;
import com.sope.sope_ecommerce_backend.dto.request.CreateMomoRequest;
import com.sope.sope_ecommerce_backend.dto.request.PaymentRequest;
import com.sope.sope_ecommerce_backend.dto.response.CreateMomoResponse;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import com.sope.sope_ecommerce_backend.utils.HmacUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component("MOMO")
@RequiredArgsConstructor
public class MOMOGateway implements PaymentGateway<CreateMomoResponse>{
    @Value("${payment-gateway.momo.endpoint:https://test.payment.momo.vn/v2/gateway/api/create}")
    private String endpoint;
    @Value("${payment-gateway.momo.partner-code:your_partner_code}")
    private String partnerCode;
    @Value("${payment-gateway.momo.access-key:your_access_key}")
    private String accessKey;
    @Value("${payment-gateway.momo.secret-key:your_secret_key}")
    private String secretKey;
    @Value("${payment-gateway.momo.return-url:http://your-site.com/api/payments/callback/MOMO}")
    private String redirectUrl;

    @Value("${payment-gateway.momo.ipn-url:http://your-site.com/api/payments/ipn/MOMO}")
    private String ipnUrl;

    private final MomoApi momoApi;

    @Override
    public PaymentProvider getProvider() { return PaymentProvider.MOMO; }


    @Override
    public CreateMomoResponse createPaymentIntent(PaymentRequest request) {

        String orderId = request.paymentId().toString();
        String orderInfo = request.orderInfo() != null ? request.orderInfo() : "Payment Sope";
        String requestId =  request.requestId();
        String extraData = "";
        String amount = String.valueOf(request.amount().longValue());

        String rawSignature =
                "accessKey=" + accessKey +
                        "&amount=" + request.amount().longValue() +
                        "&extraData=" + (extraData == null ? "" : extraData) +
                        "&ipnUrl=" + ipnUrl +
                        "&orderId=" + orderId +
                        "&orderInfo=" + orderInfo +
                        "&partnerCode=" + partnerCode +
                        "&redirectUrl=" + redirectUrl +
                        "&requestId=" + requestId +
                        "&requestType=captureWallet";

        String secureHash = HmacUtil.hmacSha256Hex(rawSignature, secretKey);



        CreateMomoRequest momoRequest = CreateMomoRequest.builder()
                .partnerCode(partnerCode)
                .requestType("captureWallet")
                .ipnUrl(ipnUrl)
                .redirectUrl(redirectUrl)
                .orderId(orderId)
                .amount(amount)
                .orderInfo(orderInfo)
                .requestId(requestId)
                .extraData(extraData)
                .signature(secureHash)
                .lang("vi")
                .build();

        return momoApi.createPaymentIntent(momoRequest);
    }

    @Override
    public PaymentStatus mapStatus(String providerStatus) {
        return "0".equals(providerStatus) ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
    }

    @Override
    public boolean verifyCallback(Map<String, String> p) {
         // IPN MoMo (v2) signature raw form (tham chiếu tài liệu MoMo):
        // raw = accessKey=...&amount=...&extraData=...&message=...&orderId=...&orderInfo=...&orderType=...&partnerCode=...&payType=...&requestId=...&responseTime=...&resultCode=...&transId=...
        String sign = p.getOrDefault("signature", "");
        String raw = "accessKey=" + p.getOrDefault("accessKey","")
                + "&amount=" + p.getOrDefault("amount","")
                + "&extraData=" + p.getOrDefault("extraData","")
                + "&message=" + p.getOrDefault("message","")
                + "&orderId=" + p.getOrDefault("orderId","")
                + "&orderInfo=" + p.getOrDefault("orderInfo","")
                + "&orderType=" + p.getOrDefault("orderType","")
                + "&partnerCode=" + p.getOrDefault("partnerCode","")
                + "&payType=" + p.getOrDefault("payType","")
                + "&requestId=" + p.getOrDefault("requestId","")
                + "&responseTime=" + p.getOrDefault("responseTime","")
                + "&resultCode=" + p.getOrDefault("resultCode","")
                + "&transId=" + p.getOrDefault("transId","");

        String expected = HmacUtil.hmacSha256Hex(raw, secretKey);
        return expected.equals(sign);
    }
}
