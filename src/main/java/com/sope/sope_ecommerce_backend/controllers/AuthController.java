package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.UserLoginRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserRegisterRequest;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

}
