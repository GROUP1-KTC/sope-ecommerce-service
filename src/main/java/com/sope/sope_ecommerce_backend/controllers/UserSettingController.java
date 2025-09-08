package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.UserSettingRequest;
import com.sope.sope_ecommerce_backend.dto.response.UserSettingResponse;
import com.sope.sope_ecommerce_backend.services.UserSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/settings")
@RequiredArgsConstructor
public class UserSettingController {

    private final UserSettingService userSettingService;

    @GetMapping
    public ResponseEntity<UserSettingResponse> getUserSetting(@PathVariable UUID userId) {
        return ResponseEntity.ok(userSettingService.getUserSetting(userId));
    }

    @PatchMapping
    public ResponseEntity<Void> updateUserSettingPartial(
            @PathVariable UUID userId,
            @RequestBody UserSettingRequest request) {
        userSettingService.updateUserSetting(userId, request);
        return ResponseEntity.noContent().build();
    }

}

