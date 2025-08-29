package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.*;
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
import com.sope.sope_ecommerce_backend.services.UserSettingService;
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
    private final UserSettingService userSettingService;


    @Override
    public UserResponse register(UserRegisterRequest request) throws MessagingException {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        AppUser appUser = userMapper.toEntity(request);
        appUser.setPassword(passwordEncoder.encode(request.password()));
        appUser.setStatus(UserStatus.ACTIVE);

        Role userRoleEntity = roleRepository.findByRoleName(RoleName.USER)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.USER)));

        UserRole userRole = UserRole.builder()
                .user(appUser)
                .role(userRoleEntity)
                .grantedBy("SYSTEM")
                .build();

        appUser.getUserRoles().add(userRole);

        appUser = userRepository.save(appUser);

        userSettingService.createDefaultUserSetting(appUser);

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
            AppUser appUser = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            appUser.getUsername(),
                            request.password()
                    )
            );


        UserDetails userDetails = userDetailsService.loadUserByUsername(appUser.getUsername());

        String accessToken = jwtProvider.generateToken(userDetails, appUser.getId());

        String refreshToken = jwtProvider.generateRefreshToken(userDetails, appUser.getId());

        redisService.set("refresh:" + refreshToken, appUser.getId().toString(), 7, TimeUnit.DAYS);

        return userMapper.toLoginResponse(appUser, accessToken, refreshToken);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Incorrect username or password", e);
        }
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

    @Override
    public void changePassword(ChangePasswordRequest request) {
        AppUser user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.email()));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) throws MessagingException {
        AppUser user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.email()));

        String otp = OtpUtil.generateOtp(6);

        String redisKey = "otp:reset:" + user.getEmail();
        redisService.set(redisKey, otp, 5, TimeUnit.MINUTES);

        emailService.sendVerificationEmail(user.getEmail(), otp);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        String redisKey = "otp:reset:" + request.email();
        Object otpInRedis = redisService.get(redisKey);

        if (otpInRedis == null) {
            throw new RuntimeException("OTP expired or invalid");
        }

        if (!request.otp().equals(otpInRedis.toString())) {
            throw new RuntimeException("OTP is incorrect");
        }

        AppUser user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        // Xoá OTP sau khi dùng
        redisService.delete(redisKey);
    }

    @Override
    public void sendOtp(String email) throws MessagingException {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("This email is already in use. Please log in or use another email.");
        }

        String otp = OtpUtil.generateOtp(6);

        String redisKey = "otp:email:" + email;
        redisService.set(redisKey, otp, 5, TimeUnit.MINUTES);

        emailService.sendVerificationEmail(email, otp);

        redisService.set("otp:verified:" + email, false, 10, TimeUnit.MINUTES);

        System.out.println("OTP for " + email + " is: " + otp);
    }

    @Override
    public void verifyOtp(String email, String otp) {
        String redisKey = "otp:email:" + email;
        Object otpInRedis = redisService.get(redisKey);

        if (otpInRedis == null) {
            throw new RuntimeException("OTP expired or invalid");
        }

        if (!otp.equals(otpInRedis.toString())) {
            throw new RuntimeException("OTP is incorrect");
        }

        redisService.set("otp:verified:" + email, true, 10, TimeUnit.MINUTES);

        redisService.delete(redisKey);
    }


}
