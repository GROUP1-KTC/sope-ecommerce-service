package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.TokenRefreshRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserLoginRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserRegisterRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserVerifyRequest;
import com.sope.sope_ecommerce_backend.dto.response.TokenRefreshResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserLoginResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserResponse;
import com.sope.sope_ecommerce_backend.entities.Role;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.entities.UserRole;
import com.sope.sope_ecommerce_backend.enums.RoleName;
import com.sope.sope_ecommerce_backend.enums.UserStatus;
import com.sope.sope_ecommerce_backend.mapper.UserMapper;
import com.sope.sope_ecommerce_backend.repositories.RoleRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.security.jwt.JwtProvider;
import com.sope.sope_ecommerce_backend.services.AuthService;
import com.sope.sope_ecommerce_backend.services.EmailService;
import com.sope.sope_ecommerce_backend.services.RedisService;
import com.sope.sope_ecommerce_backend.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final EmailService emailService;


    @Override
    public UserResponse register(UserRegisterRequest request) throws MessagingException {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        AppUser appUser = userMapper.toEntity(request);
        appUser.setPassword(passwordEncoder.encode(request.password()));
        appUser.setStatus(UserStatus.INACTIVE);

        Role userRoleEntity = roleRepository.findByRoleName(RoleName.USER)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.USER)));

        UserRole userRole = UserRole.builder()
                .user(appUser)
                .role(userRoleEntity)
                .grantedBy("SYSTEM")
                .build();

        appUser.getUserRoles().add(userRole);

        appUser = userRepository.save(appUser);

        // Sinh OTP
        String otp = OtpUtil.generateOtp(6);

        // Lưu OTP vào Redis
        redisService.set("otp:email:" + appUser.getEmail(), otp, 5, TimeUnit.MINUTES);

        // Gửi email
        emailService.sendVerificationEmail(appUser.getEmail(), otp);

        return userMapper.toResponse(appUser);
    }

    @Override
    public void verifyUser(UserVerifyRequest request) {

        String email = request.email();
        String otp = request.otp();

        String redisKey = "otp:email:" + email;
        Object otpInRedis = redisService.get(redisKey);

        if (otpInRedis == null) {
            throw new RuntimeException("OTP expired or invalid");
        }

        if (!otp.equals(otpInRedis.toString())) {
            throw new RuntimeException("OTP is incorrect");
        }

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        redisService.delete(redisKey);
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

        String accessToken = jwtProvider.generateToken(userDetails, appUser.getId());

        String refreshToken = jwtProvider.generateRefreshToken(userDetails, appUser.getId());

        redisService.set("refresh:" + refreshToken, appUser.getId().toString(), 7, TimeUnit.DAYS);

        return userMapper.toLoginResponse(appUser, accessToken, refreshToken);
    }


    @Override
    public TokenRefreshResponse refreshAccessToken(TokenRefreshRequest request) {
        String oldRefreshToken = request.refreshToken();

        String redisKey = "refresh:" + oldRefreshToken;
        Object userIdObj = redisService.get(redisKey);
        if (userIdObj == null) {
            throw new RuntimeException("Refresh token invalid or expired");
        }
        String userId = (String) userIdObj;

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtProvider.extractUsername(oldRefreshToken));

        String newAccessToken = jwtProvider.generateToken(userDetails, UUID.fromString(userId));

        String newRefreshToken = jwtProvider.generateRefreshToken(userDetails, UUID.fromString(userId));

        redisService.set("refresh:" + newRefreshToken, userId, 7, TimeUnit.DAYS);
        redisService.delete(redisKey);

        return new TokenRefreshResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(String refreshToken) {
        String redisKey = "refresh:" + refreshToken;
        redisService.delete(redisKey);
    }
}
