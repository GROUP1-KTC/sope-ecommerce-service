package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.entities.Role;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
public record UserLoginResponse (
     String username,
     String access_token,
     String refresh_token,
     List<String> roles

){}
