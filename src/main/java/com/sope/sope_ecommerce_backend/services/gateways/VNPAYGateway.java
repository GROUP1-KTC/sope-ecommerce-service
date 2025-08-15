package com.sope.sope_ecommerce_backend.services.gateways;

import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import com.sope.sope_ecommerce_backend.utils.HmacUtil;
import com.sope.sope_ecommerce_backend.utils.ParamUtil;
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

@Component("VNPAY")
public class VNPAYGateway implements  PaymentGateway{

    @Value("${vnpay.payUrl:https://sandbox.vnpayment.vn/paymentv2/vpcpay.html}")
    private String payUrl;
    @Value("${vnpay.tmnCode}")
    private String tmnCode;
    @Value("${vnpay.secretKey}")
    private String secretKey;
    @Value("${vnpay.returnUrl}")
    private String returnUrl;
    @Value("${vnpay.locale:vn}")
    private String locale;
    @Value("${vnpay.currCode:VND}")
    private String currCode;
    @Value("${vnpay.command:pay}")
    private String command;
    @Value("${vnpay.version:2.1.0}")
    private String version;

    @Override
    public PaymentProvider getProvider() { return PaymentProvider.VNPAY; }

    @Override
    public String[] createPaymentIntent(BigDecimal amount, String idempotencyKey) {
        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", version);
        params.put("vnp_Command", command);
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount", amount.movePointRight(2).toPlainString()); // x100
        params.put("vnp_CurrCode", currCode);
        params.put("vnp_TxnRef", idempotencyKey);
        params.put("vnp_OrderInfo", "Payment for order " + idempotencyKey);
        params.put("vnp_OrderType", "billpayment");
        params.put("vnp_Locale", locale);
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", "0.0.0.0"); // TODO: lấy IP thực tế từ request
        params.put("vnp_CreateDate", DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));

        // data để ký: query đã URL-encode value, sort key ASC, KHÔNG gồm vnp_SecureHash
        String dataToSign = ParamUtil.toQueryString(params, true);
        String secureHash = HmacUtil.hmacSha512Hex(dataToSign, secretKey);

        String payRedirect = payUrl + "?" + dataToSign + "&vnp_SecureHash=" + secureHash;
        return new String[]{payRedirect, idempotencyKey};
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
