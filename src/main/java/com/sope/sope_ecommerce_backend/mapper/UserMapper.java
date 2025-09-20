package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.UserRegisterRequest;
import com.sope.sope_ecommerce_backend.dto.response.UserInformationResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.entities.Role;
import com.sope.sope_ecommerce_backend.entities.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default List<String> mapRoles(Set<UserRole> userRoles) {
        return userRoles.stream()
                .map(userRole -> userRole.getRole().getRoleName().name())
                .toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "userRoles", ignore = true)
    AppUser toEntity(UserRegisterRequest request);

    UserResponse toResponse(AppUser appUser);

    UserInformationResponse toInfoResponse(AppUser appUser);

    @Mappings({
            @Mapping(target = "roles", expression = "java(mapRoles(appUser.getUserRoles()))"),
            @Mapping(target = "access_token", source = "access_token"),
            @Mapping(target = "refresh_token", source = "refresh_token")
    })
    UserLoginResponse toLoginResponse(AppUser appUser, String access_token, String refresh_token);
}
