package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.services.CookieService;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CookieServiceImpl implements CookieService {

    private static final int REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60;

    @Override
    public ResponseCookie createRefreshCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // production
                .path("/")
                .maxAge(REFRESH_TOKEN_MAX_AGE)
                .sameSite("Lax")
                .build();
    }


    @Override
    public List<ResponseCookie> clearAuthCookies() {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return List.of(accessTokenCookie, refreshTokenCookie);
    }
}
