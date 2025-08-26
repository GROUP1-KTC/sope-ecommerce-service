package com.sope.sope_ecommerce_backend.services;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendVerificationEmail(String to, String otp) throws MessagingException;
    void sendResetPasswordEmail(String to, String otp) throws MessagingException;
}
