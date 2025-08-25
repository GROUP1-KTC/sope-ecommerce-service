package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.UserStatusRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserUpdateRecord;
import com.sope.sope_ecommerce_backend.dto.response.UserInformationResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.enums.UserStatus;
import com.sope.sope_ecommerce_backend.mapper.UserMapper;
import com.sope.sope_ecommerce_backend.repositories.RoleRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.security.jwt.JwtProvider;
import com.sope.sope_ecommerce_backend.security.user.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;

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
    public UserResponse getUserById(UUID id) {
        AppUser appUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponse(appUser);
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

        if (request.username() != null) {
            appUser.setUsername(request.username());
        }
        if (request.email() != null) {
            appUser.setEmail(request.email());
        }
        if (request.name() != null) {
            appUser.setName(request.name());
        }
        if (request.phone() != null) {
            appUser.setPhone(request.phone());
        }
        if (request.address() != null) {
            appUser.setAddress(request.address());
        }
        if (request.note() != null) {
            appUser.setNote(request.note());
        }
        if (request.status() != null) {
            appUser.setStatus(UserStatus.valueOf(request.status()));
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
