package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.response.AuthDTO;
import com.sope.sope_ecommerce_backend.dto.request.LoginDTO;
import com.sope.sope_ecommerce_backend.dto.request.RegisterDTO;
import com.sope.sope_ecommerce_backend.services.impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<AuthDTO> register(@RequestBody RegisterDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO> login(@RequestBody LoginDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}