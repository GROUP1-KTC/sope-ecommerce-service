package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.services.CookieService;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CookieServiceImpl implements CookieService {

    private static final int ACCESS_TOKEN_MAX_AGE = 60 * 60;
    private static final int REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60;

    @Override
    public List<ResponseCookie> createAuthCookies(String accessToken, String refreshToken) {

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true) // production: true
                .path("/")
                .maxAge(ACCESS_TOKEN_MAX_AGE)
                .sameSite("Strict")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(REFRESH_TOKEN_MAX_AGE)
                .sameSite("Strict")
                .build();

        return List.of(accessTokenCookie, refreshTokenCookie);
    }

    @Override
    public List<ResponseCookie> clearAuthCookies() {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return List.of(accessTokenCookie, refreshTokenCookie);
    }
}
