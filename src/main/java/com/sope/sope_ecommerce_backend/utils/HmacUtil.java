package com.sope.sope_ecommerce_backend.utils;

// util/HmacUtil.java
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public final class HmacUtil {
    private HmacUtil() {}
    public static String hmacSha256Hex(String data, String key) {
        return toHex(hmac("HmacSHA256", data, key));
    }
    public static String hmacSha512Hex(String data, String key) {
        return toHex(hmac("HmacSHA512", data, key));
    }
    private static byte[] hmac(String alg, String data, String key) {
        try {
            Mac mac = Mac.getInstance(alg);
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), alg));
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) { throw new RuntimeException(e); }
    }
    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b: bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
