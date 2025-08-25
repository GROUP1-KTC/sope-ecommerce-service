package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.UserSettingRequest;
import com.sope.sope_ecommerce_backend.dto.response.UserSettingResponse;
import com.sope.sope_ecommerce_backend.entities.AppUser;

import java.util.UUID;

public interface UserSettingService {
    UserSettingResponse getUserSetting(UUID userId);
    void updateUserSetting(UUID userId, UserSettingRequest request);
    void createDefaultUserSetting(AppUser user);
}

