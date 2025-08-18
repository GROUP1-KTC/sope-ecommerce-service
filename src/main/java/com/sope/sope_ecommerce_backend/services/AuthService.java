package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.TokenRefreshRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserLoginRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserRegisterRequest;
import com.sope.sope_ecommerce_backend.dto.response.TokenRefreshResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(UserRegisterRequest request);
    UserLoginResponse login(UserLoginRequest request);
    TokenRefreshResponse refreshAccessToken(TokenRefreshRequest request);
    void logout(String request);
}
