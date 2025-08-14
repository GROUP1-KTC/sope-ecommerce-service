package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.TokenRefreshRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserLoginRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserRegisterRequest;
import com.sope.sope_ecommerce_backend.dto.response.TokenRefreshResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.entities.Role;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.entities.UserRole;
import com.sope.sope_ecommerce_backend.enums.RoleName;
import com.sope.sope_ecommerce_backend.mapper.UserMapper;
import com.sope.sope_ecommerce_backend.repositories.RoleRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.security.JwtUtil;
import com.sope.sope_ecommerce_backend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;


    @Override
    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        AppUser appUser = userMapper.toEntity(request);
        appUser.setPassword(passwordEncoder.encode(request.password()));

        Role userRoleEntity = roleRepository.findByRoleName(RoleName.USER)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.USER)));

        UserRole userRole = UserRole.builder()
                .user(appUser)
                .role(userRoleEntity)
                .grantedBy("SYSTEM")
                .build();

        appUser.getUserRoles().add(userRole);

        appUser = userRepository.save(appUser);

        return userMapper.toResponse(appUser);
    }



    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Incorrect username or password", e);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        AppUser appUser = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String access_token = jwtUtil.generateToken(userDetails, appUser.getId());

        String refresh_token = jwtUtil.generateRefreshToken(userDetails, appUser.getId());

        UserLoginResponse response = userMapper.toLoginResponse(appUser, access_token, refresh_token);

        return response;
    }

    @Override
    public TokenRefreshResponse refreshAccessToken(TokenRefreshRequest request) {
        String refreshToken = request.refreshToken();

        String username = jwtUtil.extractUsername(refreshToken);
        String userId = jwtUtil.extractUserId(refreshToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtUtil.validateToken(refreshToken, userDetails, userId)) {
            String newAccessToken = jwtUtil.generateToken(userDetails, UUID.fromString(userId));
            return new TokenRefreshResponse(newAccessToken);
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }
}
