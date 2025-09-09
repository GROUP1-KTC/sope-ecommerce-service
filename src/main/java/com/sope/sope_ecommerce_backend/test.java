package com.sope.sope_ecommerce_backend;

import com.sope.sope_ecommerce_backend.utils.HmacUtil;

public class test {
    public static void main(String[] args) {
        String rawSignature =
                "accessKey=" + "F8BBA842ECF85" +
                        "&amount=150000"  +
                        "&extraData=" + "userId=12345"  +
                        "&ipnUrl=" + "https://example.com/ipn"  +

                        "&orderId=ORDER_20250909_001"  +
                        "&orderInfo=" + "Thanh toán đơn hàng #001"  +
                        "&partnerCode=" + "MOMO" +
                        "&redirectUrl=" + "https://example.com/redirect"  +

                        "&requestId=REQ_20250909_001" +
                        "&requestType=captureWallet";


        String secureHash = HmacUtil.hmacSha256Hex(rawSignature, "K951B6PE1waDMi640xX08PD3vg6EkVlz");
        System.out.println(secureHash);
    }
}
