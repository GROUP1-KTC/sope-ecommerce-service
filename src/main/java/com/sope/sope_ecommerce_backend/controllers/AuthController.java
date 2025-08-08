package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.ApiResponse;
import com.sope.sope_ecommerce_backend.dto.request.UserLoginRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserRegisterRequest;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.services.UserService;
import com.sope.sope_ecommerce_backend.utils.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody UserRegisterRequest request) {
        try {
            UserResponse userResponse = userService.register(request);
            return ApiResponseUtil.success(userResponse, "User registered successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to register user", List.of(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@RequestBody UserLoginRequest request) {

        try {
            UserLoginResponse loginResponse = userService.login(request);
            return ApiResponseUtil.success(loginResponse, "User logged in successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to login user", List.of(e.getMessage()));
        }
    }

}
