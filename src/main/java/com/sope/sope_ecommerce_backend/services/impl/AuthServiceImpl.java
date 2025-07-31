package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.response.AuthDTO;
import com.sope.sope_ecommerce_backend.dto.request.LoginDTO;
import com.sope.sope_ecommerce_backend.dto.request.RegisterDTO;
import com.sope.sope_ecommerce_backend.entities.RoleEntity;
import com.sope.sope_ecommerce_backend.entities.UserEntity;
import com.sope.sope_ecommerce_backend.repositories.RoleRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;

    public AuthDTO register(RegisterDTO request) {
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        RoleEntity role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);

        AuthDTO response = new AuthDTO();
        response.setToken(jwtProvider.createToken(user.getUsername(), "ROLE_USER"));
        return response;
    }

    public AuthDTO login(LoginDTO request) {
        // Xác thực thông tin đăng nhập
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Lấy role từ danh sách quyền
        String role = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())  // Lấy tên role
                .findFirst()
                .orElse("ROLE_USER");

        // Tạo token
        String token = jwtProvider.createToken(request.getUsername(), role);

        // Trả về AuthResponse đầy đủ
        AuthDTO response = new AuthDTO();
        response.setUsername(request.getUsername());
        response.setRole(role);
        response.setToken(token);

        return response;
    }

}
