package com.sope.sope_ecommerce_backend.services.gateways;

import com.sope.sope_ecommerce_backend.client.MomoApi;
import com.sope.sope_ecommerce_backend.client.VnPayApi;
import com.sope.sope_ecommerce_backend.dto.request.CreateMomoRequest;
import com.sope.sope_ecommerce_backend.dto.request.CreateVNPayRequest;
import com.sope.sope_ecommerce_backend.dto.request.PaymentRequest;
import com.sope.sope_ecommerce_backend.dto.response.PaymentResponse;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import com.sope.sope_ecommerce_backend.utils.HmacUtil;
import com.sope.sope_ecommerce_backend.utils.ParamUtil;
import com.sope.sope_ecommerce_backend.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

import static com.sope.sope_ecommerce_backend.constant.VNPayVariable.DEFAULT_MULTIPLIER;

@Component("VNPAY")
@RequiredArgsConstructor
public class VNPAYGateway implements  PaymentGateway<PaymentResponse>{

    @Value("${payment-gateway.vnpay.pay-url:https://sandbox.vnpayment.vn/paymentv2/vpcpay.html}")
    private String payUrl;
    @Value("${payment-gateway.vnpay.tmnCode:your_tmn_code}")
    private String tmnCode;
    @Value("${payment-gateway.vnpay.secretKey:your_secret_key}")
    private String secretKey;
    @Value("${payment-gateway.vnpay.returnUrl:http://your-site.com/api/payments/callback/VNPAY}")
    private String returnUrl;
    @Value("${payment-gateway.vnpay.locale:vn:vi}")
    private String locale;
    @Value("${payment-gateway.vnpay.currCode:VND}")
    private String currCode;
    @Value("${payment-gateway.vnpay.vnp-command:pay}")
    private String command;
    @Value("${payment-gateway.vnpay.vnp-version:2.1.0}")
    private String version;

    private final VnPayApi vnPayApi;
    private final RequestUtil requestUtil;


    @Override
    public PaymentProvider getProvider() { return PaymentProvider.VNPAY; }

    @Override
    public PaymentResponse createPaymentIntent(PaymentRequest request) {
        String orderInfo = request.orderInfo();

        String vnp_TxnRef = request.idempotencyKey();
        String vnp_IpAddr = requestUtil.getClientIp();

        String vnpCurrCode = "VND";
        String vnpLocale = "vn";
        String vnpOrderType = "other";

        String requestId =  request.idempotencyKey();
        String amount = String.valueOf(request.amount().multiply(BigDecimal.valueOf(DEFAULT_MULTIPLIER)).longValue());

//        data = vnp_RequestId + “|” + vnp_Version + “|” +
//                vnp_Command + “|” + vnp_TmnCode + “|” +
//                vnp_TxnRef + “|” + vnp_TransactionDate + “|” +
//                vnp_CreateDate + “|” + vnp_IpAddr + “|” +
//                vnp_OrderInfo;

//        String rawSignature =
//                "accessKey=" + accessKey +
//                        "&amount=" + request.amount().longValue() +
//                        "&extraData=" + (extraData == null ? "" : extraData) +
//                        "&ipnUrl=" + ipnUrl +
//                        "&orderId=" + orderId +
//                        "&orderInfo=" + orderInfo +
//                        "&partnerCode=" + partnerCode +
//                        "&redirectUrl=" + redirectUrl +
//                        "&requestId=" + requestId +
//                        "&requestType=captureWallet";

//        StringBuilder hashData = new StringBuilder();
//        for (Map.Entry<String, String> e : req.toSortedParamMap(false).entrySet()) {
//            if (e.getValue() == null || e.getValue().isEmpty()) continue;
//            if (hashData.length() > 0) hashData.append('&');
//            hashData.append(e.getKey()).append('=').append(e.getValue());
//        }


//        String secureHash = HmacUtil.hmacSha512Hex(rawSignature, secretKey);




//        CreateVNPayRequest vnPayRequest = CreateVNPayRequest.builder()
//                .partnerCode(partnerCode)
//                .requestType("captureWallet")
//                .ipnUrl(ipnUrl)
//                .redirectUrl(redirectUrl)
//                .orderId(orderId)
//                .amount(amount)
//                .orderInfo(orderInfo)
//                .requestId(requestId)
//                .extraData(extraData)
//                .signature(secureHash)
//                .lang("vi")
//                .build();
//
//        return vnPayApi.createPaymentIntent(momoRequest);

        return null;
    }


    @Override
    public PaymentStatus mapStatus(String code) {
        // VNPay: vnp_ResponseCode == "00" là thành công
        return "00".equals(code) ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
    }

    @Override
    public boolean verifyCallback(Map<String, String> flat) {
        // Loại bỏ hash và kiểu hash
        Map<String, String> data = ParamUtil.copyWithout(flat, "vnp_SecureHash", "vnp_SecureHashType");
        String dataToSign = ParamUtil.toQueryString(new TreeMap<>(data), true);
        String expected = HmacUtil.hmacSha512Hex(dataToSign, secretKey);
        String received = flat.getOrDefault("vnp_SecureHash", "");
        return expected.equalsIgnoreCase(received);
    }
}
