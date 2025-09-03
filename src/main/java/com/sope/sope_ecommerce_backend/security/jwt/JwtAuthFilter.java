package com.sope.sope_ecommerce_backend.security.jwt;

import com.sope.sope_ecommerce_backend.security.user.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    // Các route không cần filter
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/swagger-ui/",
            "/v3/api-docs"
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getServletPath();
        if (isExcludedPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Optional.ofNullable(jwtProvider.getTokenFromHeader(request))
                    .filter(StringUtils::hasText)
                    .filter(jwtProvider::validateToken)
                    .map(token -> {
                        String username = jwtProvider.extractUsername(token);
                        String userId = jwtProvider.extractUserId(token);
                        return new String[]{token, username, userId};
                    })
                    .filter(data -> SecurityContextHolder.getContext().getAuthentication() == null)
                    .ifPresent(data -> setAuthentication(data[0], data[1], data[2], request));
        } catch (AuthenticationException ex) { // 401 Unauthorized
        SecurityContextHolder.clearContext();
        log.error("Unauthorized error: {}", ex.getMessage());

    } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            log.error("Internal error: {}", ex.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, ex);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }

    private void setAuthentication(String token, String username, String userId, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtProvider.validateTokenWithUser(token, userDetails, userId)) {
            Claims claims = jwtProvider.getClaims(token);
            List<String> roles = Optional.ofNullable(claims.get("roles", List.class))
                    .orElse(List.of());

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);

            log.info("Authentication set for user {} with roles {}", username, roles);
        }
    }


}

