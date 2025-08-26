package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.*;
import com.sope.sope_ecommerce_backend.dto.response.TokenRefreshResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import jakarta.mail.MessagingException;

public interface AuthService {

    UserResponse register(UserRegisterRequest request) throws MessagingException;
    UserLoginResponse login(UserLoginRequest request);
    TokenRefreshResponse refreshAccessToken(TokenRefreshRequest request);
    void logout(String request);
    void verifyUser(UserVerifyRequest request);
    void changePassword(ChangePasswordRequest request);
    void forgotPassword(ForgotPasswordRequest request) throws MessagingException;
    void resetPassword(ResetPasswordRequest request);

}
