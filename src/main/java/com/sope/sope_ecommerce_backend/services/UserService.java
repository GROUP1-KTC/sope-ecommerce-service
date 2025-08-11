package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.UserStatusRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserUpdateRecord;
import com.sope.sope_ecommerce_backend.dto.response.UserInformationResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserInformationResponse getCurrentUserInfo();
    UserResponse getUserById(UUID id);
    List<UserInformationResponse> getAllUsers();
    UserInformationResponse updateUser(UUID id,UserUpdateRecord request);
    void changeUserStatus(UUID id, UserStatusRequest request);
}
