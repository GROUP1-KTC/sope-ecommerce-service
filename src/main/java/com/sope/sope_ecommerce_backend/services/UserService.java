package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.FaceAuthRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserStatusRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserUpdateRecord;
import com.sope.sope_ecommerce_backend.dto.response.UserInformationResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.enums.RoleName;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserInformationResponse getCurrentUserInfo();
    UserResponse getUserById(UUID id);
    AppUser getUserEntityById(UUID id);

    UUID getCurrentUserId();
    List<UserInformationResponse> getAllUsers();
    UserInformationResponse updateUser(UUID id,UserUpdateRecord request);
    void changeUserStatus(UUID id, UserStatusRequest request);

    AppUser getOrCreateGuestUser(String email, String fullName, String phone);
    UserInformationResponse updateUserAvatar(String avatarUrl);

    void addRoleToUser(UUID userId, RoleName roleName, String grantedBy);

    UserInformationResponse updateUserFaceAuth(UUID userId, FaceAuthRequest request);
}
