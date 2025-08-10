package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.UserRegisterRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserStatusRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserUpdateRecord;
import com.sope.sope_ecommerce_backend.dto.response.UserInformationResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserInformationResponse> getCurrentUserInfo() {
        return ResponseEntity.ok(userService.getCurrentUserInfo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<UserInformationResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserInformationResponse> updateUser(@PathVariable UUID id, @RequestBody UserUpdateRecord request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }


    @PatchMapping("status/{id}")
    public ResponseEntity<Void> changeUserStatus(@PathVariable UUID id ,@RequestBody UserStatusRequest request) {
        userService.changeUserStatus(id, request);
        return ResponseEntity.noContent().build();
    }
}
