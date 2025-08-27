package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.ApiResponse;
import com.sope.sope_ecommerce_backend.dto.request.*;
import com.sope.sope_ecommerce_backend.dto.response.TokenRefreshResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.services.AuthService;
import com.sope.sope_ecommerce_backend.utils.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody UserRegisterRequest request) {
        try {
            UserResponse userResponse = authService.register(request);
            return ApiResponseUtil.success(userResponse, "User registered successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to register user", List.of(e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestBody UserVerifyRequest request) {
        try {
            authService.verifyUser(request);
            return ApiResponseUtil.success("Email verified successfully.", "Verification successful.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to verify email", List.of(e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@RequestBody UserLoginRequest request) {

        try {
            UserLoginResponse loginResponse = authService.login(request);
            return ApiResponseUtil.success(loginResponse, "User logged in successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to login user", List.of(e.getMessage()));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshAccessToken(@RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse response = authService.refreshAccessToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody TokenRefreshRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        try {
            authService.changePassword(request);
            return ApiResponseUtil.success("Password changed successfully.", "Change password successful.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to change password", List.of(e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            authService.forgotPassword(request);
            return ApiResponseUtil.success("OTP has been sent to your email.", "Forgot password request successful.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to send OTP", List.of(e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request);
            return ApiResponseUtil.success("Password reset successfully.", "Reset successful.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to reset password", List.of(e.getMessage()));
        }
    }


}
