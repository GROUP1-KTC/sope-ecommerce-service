package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.entities.AppUser;
import jakarta.mail.MessagingException;

import java.util.Map;

public interface EmailService {
    void sendVerificationEmail(String to, String otp) throws MessagingException;
    void sendResetPasswordEmail(String to, String otp) throws MessagingException;

    void sendOrderConfirmationEmail(String toEmail, Map<String, Object> model) throws MessagingException;

    void sendAccountCreationEmail(AppUser user) throws MessagingException;
}
