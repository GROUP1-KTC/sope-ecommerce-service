package com.sope.sope_ecommerce_backend.security.secret;

import com.sope.sope_ecommerce_backend.security.jwt.JwtProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RsaKeyUtil {
    private static final Logger logger = LoggerFactory.getLogger(RsaKeyUtil.class);
    private final JwtProperties jwtProperties;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public RsaKeyUtil(JwtProperties jwtProperties) throws Exception {
        this.jwtProperties = jwtProperties;
        this.privateKey = loadPrivateKey();
        this.publicKey = loadPublicKey();
    }

    private PrivateKey loadPrivateKey() throws Exception {
        String privateKeyPEM = jwtProperties.getPrivateKey();
        if (privateKeyPEM == null || privateKeyPEM.trim().isEmpty()) {
            logger.error("Khóa riêng không được tìm thấy trong cấu hình jwt.private-key");
            throw new IllegalArgumentException("Khóa riêng không được tìm thấy trong cấu hình jwt.private-key");
        }
        try {
            // Loại bỏ tiêu đề PEM nếu có
            String cleanedKey = privateKeyPEM
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            logger.debug("Đã làm sạch khóa riêng: {} ký tự", cleanedKey.length());
            byte[] decoded = Base64.getDecoder().decode(cleanedKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            logger.error("Lỗi khi giải mã khóa riêng: {}", e.getMessage(), e);
            throw new IllegalStateException("Lỗi khi giải mã khóa riêng: " + e.getMessage(), e);
        }
    }

    private PublicKey loadPublicKey() throws Exception {
        String publicKeyPEM = jwtProperties.getPublicKey();
        if (publicKeyPEM == null || publicKeyPEM.trim().isEmpty()) {
            logger.error("Khóa công khai không được tìm thấy trong cấu hình jwt.public-key");
            throw new IllegalArgumentException("Khóa công khai không được tìm thấy trong cấu hình jwt.public-key");
        }
        try {
            // Loại bỏ tiêu đề PEM nếu có
            String cleanedKey = publicKeyPEM
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            logger.debug("Đã làm sạch khóa công khai: {} ký tự", cleanedKey.length());
            byte[] decoded = Base64.getDecoder().decode(cleanedKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            logger.error("Lỗi khi giải mã khóa công khai: {}", e.getMessage(), e);
            throw new IllegalStateException("Lỗi khi giải mã khóa công khai: " + e.getMessage(), e);
        }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @PostConstruct
    public void logKeys() {
        logger.info("RsaKeyUtil initialized. PrivateKey: {}, PublicKey: {}",
                privateKey != null ? "Present" : "Missing",
                publicKey != null ? "Present" : "Missing");
    }
}