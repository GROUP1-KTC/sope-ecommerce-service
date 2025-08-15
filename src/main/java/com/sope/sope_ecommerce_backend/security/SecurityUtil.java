package com.sope.sope_ecommerce_backend.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtil {
    public CustomUserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
                return userDetails;
            }
        }
        throw new RuntimeException("User is not authenticated");
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

}
