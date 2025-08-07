package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.UserLoginRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserRegisterRequest;
import com.sope.sope_ecommerce_backend.dto.response.UserInformationResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse register(UserRegisterRequest request);
    UserLoginResponse login(UserLoginRequest request);
    UserInformationResponse getCurrentUserInfo();
    UserResponse getUserById(UUID id);
    List<UserInformationResponse> getAllUsers();
    UserResponse updateUser(UUID id, UserRegisterRequest request);
    void deleteUser(UUID id);
}
