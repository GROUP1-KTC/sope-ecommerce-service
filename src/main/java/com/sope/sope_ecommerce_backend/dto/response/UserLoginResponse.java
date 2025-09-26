package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.entities.Role;

import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
public record UserLoginResponse (
        UUID id,
     String username,
     String access_token,
     String refresh_token,
     List<String> roles,
        String tempToken,
        boolean twoFaRequired,
        String faceAuthId

){}
