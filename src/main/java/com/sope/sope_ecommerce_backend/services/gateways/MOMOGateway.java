package com.sope.sope_ecommerce_backend.services.gateways;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import com.sope.sope_ecommerce_backend.utils.HmacUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Component("MOMO")
public class MOMOGateway implements PaymentGateway{
    @Value("${momo.endpoint:https://test.payment.momo.vn/v2/gateway/api/create}")
    private String endpoint;
    @Value("${momo.partnerCode:your_partner_code}")
    private String partnerCode;
    @Value("${momo.accessKey:your_access_key}")
    private String accessKey;
    @Value("${momo.secretKey:your_secret_key}")
    private String secretKey;
    @Value("${momo.redirectUrl:http://your-site.com/api/payments/callback/MOMO}")
    private String redirectUrl;

    @Value("${momo.ipnUrl}")
    private String ipnUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public PaymentProvider getProvider() { return PaymentProvider.MOMO; }


    @Override
    public String[] createPaymentIntent(BigDecimal amount, String idempotencyKey) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("partnerCode", partnerCode);
        body.put("accessKey", accessKey);
        body.put("requestId", idempotencyKey);
        body.put("amount", amount.toString());
        body.put("orderId", idempotencyKey);
        body.put("orderInfo", "Payment for order");
        body.put("redirectUrl", redirectUrl);
        body.put("ipnUrl", redirectUrl); // Same for notify
        body.put("requestType", "captureWallet");
        body.put("extraData", "");


        String raw = "accessKey=" + body.get("accessKey")
                + "&amount=" + body.get("amount")
                + "&extraData=" + body.get("extraData")
                + "&ipnUrl=" + body.get("ipnUrl")
                + "&orderId=" + body.get("orderId")
                + "&orderInfo=" + body.get("orderInfo")
                + "&partnerCode=" + body.get("partnerCode")
                + "&redirectUrl=" + body.get("redirectUrl")
                + "&requestId=" + body.get("requestId")
                + "&requestType=" + body.get("requestType");

        body.put("signature", HmacUtil.hmacSha256Hex(raw, secretKey));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> resp = restTemplate.postForEntity(endpoint, new HttpEntity<>(body, headers), Map.class);


        Map response = Objects.requireNonNull(resp.getBody());
        String payUrl = Objects.toString(response.get("payUrl"), null);
        String orderId = Objects.toString(response.get("orderId"), null);
        // Optional: String qrCodeUrl = Objects.toString(r.get("qrCodeUrl"), null);

        return new String[]{payUrl, orderId};
    }

    @Override
    public PaymentStatus mapStatus(String providerStatus) {
        // MoMo: resultCode == "0" là thành công
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
