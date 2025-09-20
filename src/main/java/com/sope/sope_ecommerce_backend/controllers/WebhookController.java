package com.sope.sope_ecommerce_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sope.sope_ecommerce_backend.dto.request.GoShipDataUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


@Slf4j
@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Value("${shipping-unit.goship.client-secret:your_goship_client_secret_here}")
    private String clientSecret;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/shipping-status")
    public ResponseEntity<String> handleWebhook(@RequestHeader(value = "x-goship-hmac-sha256", required = false) String webhookHmac,
                                                @RequestBody(required = false) String body) {



        boolean verified = verifyWebhook(body, webhookHmac);

        if (!verified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid HMAC");
        }

        try {
            GoShipDataUpdateRequest data = objectMapper.readValue(body, GoShipDataUpdateRequest.class);

            log.info("Received webhook for orderId={}, status={}, message={}", data.orderId(), data.status(), data.message());

            return ResponseEntity.ok("Webhook verified and processed for orderId=" + data.orderId());
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request format");
        }
    }

    private boolean verifyWebhook(String payload, String webhookHmac) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec =
                    new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);

            byte[] rawHmac = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String computedHmac = Base64.getEncoder().encodeToString(rawHmac);

            return computedHmac.equals(webhookHmac);
        } catch (Exception e) {
            return false;
        }
    }
}
