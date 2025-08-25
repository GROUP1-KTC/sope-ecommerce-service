package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final int OTP_EXPIRY_MINUTES = 5;

    @Override
    public void sendVerificationEmail(String toEmail, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Xác thực tài khoản - Mã OTP");

        Context context = new Context();
        context.setVariable("email", toEmail);
        context.setVariable("otp", otp);
        context.setVariable("expiryMinutes", OTP_EXPIRY_MINUTES);

        String htmlContent = templateEngine.process("verificationEmail", context);

        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }
}

