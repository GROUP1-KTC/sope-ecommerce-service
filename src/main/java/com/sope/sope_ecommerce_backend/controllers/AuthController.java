package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.ApiResponse;
import com.sope.sope_ecommerce_backend.dto.request.*;
import com.sope.sope_ecommerce_backend.dto.response.TokenRefreshResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.services.AuthService;
import com.sope.sope_ecommerce_backend.services.CookieService;
import com.sope.sope_ecommerce_backend.services.impl.CookieServiceImpl;
import com.sope.sope_ecommerce_backend.utils.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    private CookieServiceImpl cookieService;

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(@RequestBody UserSendOtpRequest request) {
        try {
            authService.sendOtp(request.email());
            return ApiResponseUtil.success("OTP sent successfully.", "OTP sent");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to send OTP", List.of(e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@RequestBody UserVerifyRequest request) {
        try {
            authService.verifyOtp(request.email(), request.otp());
            return ApiResponseUtil.success("Email verified successfully.", "OTP verified");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to verify OTP", List.of(e.getMessage()));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody UserRegisterRequest request) {
        try {
            UserResponse userResponse = authService.register(request);
            return ApiResponseUtil.success(userResponse, "User registered successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to register user", List.of(e.getMessage()));
        }
    }

    @PostMapping("/verify-user")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestBody UserVerifyRequest request) {
        try {
            authService.verifyUser(request);
            return ApiResponseUtil.success("Email verified successfully.", "Verification successful.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to verify email", List.of(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponseFE>> login(@RequestBody UserLoginRequest request) {
        try {
            UserLoginResponse loginResponse = authService.login(request);

            UserLoginResponseFE responseFE = new UserLoginResponseFE(
                    loginResponse.id(),
                    loginResponse.username(),
                    loginResponse.roles(),
                    loginResponse.access_token()
            );

            // Tạo cookies
            ResponseCookie refreshCookie = cookieService.createRefreshCookie(loginResponse.refresh_token());

            ResponseEntity<ApiResponse<UserLoginResponseFE>> response = ApiResponseUtil.success(responseFE, "User logged in successfully");

            ResponseEntity.BodyBuilder builder = ResponseEntity.status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString());

            return builder.body(response.getBody());


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
