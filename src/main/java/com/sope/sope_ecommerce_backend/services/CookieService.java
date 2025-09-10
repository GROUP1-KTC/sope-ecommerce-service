package com.sope.sope_ecommerce_backend.services;

import org.springframework.http.ResponseCookie;

import java.util.List;

public interface CookieService {
    List<ResponseCookie> clearAuthCookies();
    ResponseCookie createRefreshCookie(String cookie);
}
