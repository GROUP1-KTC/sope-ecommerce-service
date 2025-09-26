package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.*;
import com.sope.sope_ecommerce_backend.dto.response.TokenRefreshResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseCookie;

import java.util.List;

public interface AuthService {

    UserResponse register(UserRegisterRequest request) throws MessagingException;
    UserLoginResponse login(UserLoginRequest request);
    TokenRefreshResponse refreshAccessToken(String refreshToken);
    List<ResponseCookie> logout(String request);
    void verifyUser(UserVerifyRequest request);
    void changePassword(ChangePasswordRequest request);
    void forgotPassword(ForgotPasswordRequest request) throws MessagingException;
    void resetPassword(ResetPasswordRequest request);
    void sendOtp(String email) throws MessagingException;
    void verifyOtp(String email, String otp) throws MessagingException;
    UserLoginResponse confirmFace(String tempToken);
}
