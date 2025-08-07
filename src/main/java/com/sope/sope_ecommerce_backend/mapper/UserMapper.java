package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.UserRegisterRequest;
import com.sope.sope_ecommerce_backend.dto.response.UserInformationResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "name", source = "name")
    User toEntity(UserRegisterRequest request);

    UserResponse toResponse(User user);

    UserInformationResponse toInfoResponse(User response);

    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(com.sope.sope_ecommerce_backend.entities.Role::getRoleName).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "jwt", ignore = true)
    UserLoginResponse toLoginResponse(User user);
}