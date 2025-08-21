package com.sope.sope_ecommerce_backend.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class RandomUtil {
    /**
     * Generates a random alphanumeric string of the specified length.
     *
     * @param length the length of the random string to generate
     * @return a random alphanumeric string
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }


    public static String generateRandomCode() {
        return generateRandomString(8);
    }

    public static String generateKey(String prefix, String userId, boolean isTimestamped) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        String suffix = isTimestamped ? LocalDateTime.now().format(formatter) : UUID.randomUUID().toString();

        return prefix + "-" + userId + "-" + suffix;
    }
}
