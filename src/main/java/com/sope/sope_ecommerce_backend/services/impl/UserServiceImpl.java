package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.UserStatusRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserUpdateRecord;
import com.sope.sope_ecommerce_backend.dto.response.UserInformationResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.entities.AppUser;

import com.sope.sope_ecommerce_backend.enums.UserStatus;

import com.sope.sope_ecommerce_backend.mapper.UserMapper;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.security.user.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserInformationResponse getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User chưa đăng nhập");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        AppUser appUser = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại: " + userDetails.getUserId()));

        return userMapper.toInfoResponse(appUser);
    }

    @Override
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User chưa đăng nhập");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUserId();
    }



    @Override
    public UserResponse getUserById(UUID id) {
        AppUser appUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponse(appUser);
    }


    @Override
    public AppUser getUserEntityById(UUID id) {
        AppUser appUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return appUser;
    }

    @Override
    public AppUser getOrCreateGuestUser(String email, String fullName, String phone) {
        AppUser guestUser = userRepository.findByEmail(email).orElse(null);
        if (guestUser == null) {
            guestUser = AppUser.builder()
                    .email(email)
                    .name(fullName)
//                    .roles()
                    .status(UserStatus.INACTIVE)
                    .build();

            userRepository.save(guestUser);
        }
        return guestUser;
    }

    @Override
    public List<UserInformationResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toInfoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserInformationResponse updateUser(UUID id, UserUpdateRecord request) {
        AppUser appUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.name() != null) {
            appUser.setName(request.name());
        }

        if (request.gender() != null) {
            appUser.setGender(request.gender());
        }

        if (request.birthday() != null) {
            appUser.setBirthday(request.birthday());
        }

        if (request.avatarUrl() != null) {
            appUser.setAvatarUrl(request.avatarUrl());
        }

        appUser = userRepository.save(appUser);
        return userMapper.toInfoResponse(appUser);
    }

    @Override
    public void changeUserStatus(UUID id, UserStatusRequest request) {
        AppUser appUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        appUser.setStatus(UserStatus.valueOf(request.status()));
        userRepository.save(appUser);
    }
}
