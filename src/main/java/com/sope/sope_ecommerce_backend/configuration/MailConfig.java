package com.sope.sope_ecommerce_backend.configuration;

import com.google.auth.oauth2.UserCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${EMAIL}")
    private String email;

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    @Value("${REFRESH_TOKEN}")
    private String refreshToken;

    @Bean
    public JavaMailSender javaMailSender() {
        // Tạo Google OAuth2 credentials
        UserCredentials userCredentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refreshToken)
                .build();

        String accessToken;
        try {
            accessToken = userCredentials.refreshAccessToken().getTokenValue();
        } catch (Exception e) {
            throw new RuntimeException("Không thể lấy access token từ refresh token", e);
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(email);
        mailSender.setPassword(accessToken);
        Properties props = mailSender.getJavaMailProperties(

        );
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        props.put("mail.debug", "true");
        return mailSender;
    }
}

