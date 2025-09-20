package com.sope.sope_ecommerce_backend.services.impl;


import com.sope.sope_ecommerce_backend.dto.request.UserSettingRequest;
import com.sope.sope_ecommerce_backend.dto.response.UserSettingResponse;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.entities.UserSetting;
import com.sope.sope_ecommerce_backend.mapper.UserSettingMapper;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.repositories.UserSettingRepository;
import com.sope.sope_ecommerce_backend.services.UserSettingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSettingServiceImpl implements UserSettingService {

    private final UserSettingRepository userSettingRepository;
    private final UserRepository appUserRepository;
    private final UserSettingMapper userSettingMapper;

    @Override
    public UserSettingResponse getUserSetting(UUID userId) {
        UserSetting setting = userSettingRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User setting not found for userId: " + userId));

        return userSettingMapper.toResponse(setting);
    }

    @Override
    public void updateUserSetting(UUID userId, UserSettingRequest request) {
        UserSetting setting = userSettingRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("UserSetting not found for user " + userId));

        if (request.orderUpdateNoti() != null) {
            setting.setOrderUpdateNoti(request.orderUpdateNoti());
        }
        if (request.promotionNoti() != null) {
            setting.setPromotionNoti(request.promotionNoti());
        }
        if (request.surveyNoti() != null) {
            setting.setSurveyNoti(request.surveyNoti());
        }

        userSettingRepository.save(setting);
    }


    public void createDefaultUserSetting(AppUser user) {
        UserSetting setting = UserSetting.builder()
                .user(user)
                .orderUpdateNoti(true)
                .promotionNoti(true)
                .surveyNoti(true)
                .build();
        userSettingRepository.save(setting);
    }
}
