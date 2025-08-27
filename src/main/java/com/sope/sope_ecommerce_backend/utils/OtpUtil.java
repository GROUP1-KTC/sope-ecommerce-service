package com.sope.sope_ecommerce_backend.utils;

import java.security.SecureRandom;

public class OtpUtil {
    private static final SecureRandom random = new SecureRandom();

    public static String generateOtp(int length) {
        String digits = "0123456789";
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(digits.charAt(random.nextInt(digits.length())));
        }
        return otp.toString();
    }
}
