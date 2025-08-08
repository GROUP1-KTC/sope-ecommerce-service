package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.UserRegisterRequest;
import com.sope.sope_ecommerce_backend.dto.response.UserInformationResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.entities.Role;
import com.sope.sope_ecommerce_backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default List<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.getRoleName().name())
                .toList();
    }


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "name", source = "name")
    User toEntity(UserRegisterRequest request);


    UserResponse toResponse(User user);

    UserInformationResponse toInfoResponse(User user);

    @Mappings({
            @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))"),
            @Mapping(target = "jwt", source = "jwt")
    })
    UserLoginResponse toLoginResponse(User user, String jwt);
}