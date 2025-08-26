package com.sope.sope_ecommerce_backend.security.jwt;

import com.sope.sope_ecommerce_backend.security.secret.RsaKeyUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private final RsaKeyUtil rsaKeyUtil;

    public JwtProvider(JwtProperties jwtProperties, RsaKeyUtil rsaKeyUtil) {
        this.jwtProperties = jwtProperties;
        this.rsaKeyUtil = rsaKeyUtil;
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public String extractUserId(String token) {
        return getClaims(token).get("userId", String.class);
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(rsaKeyUtil.getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails, UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .toList()
        );
        return createToken(claims, userDetails.getUsername(), jwtProperties.getExpiration());
    }

    public String generateRefreshToken(UserDetails userDetails, UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        return createToken(claims, userDetails.getUsername(), jwtProperties.getRefreshExpiration());
    }

    private String createToken(Map<String, Object> claims, String subject, long ttlMillis) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ttlMillis))
                .signWith(rsaKeyUtil.getPrivateKey(), io.jsonwebtoken.SignatureAlgorithm.RS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateTokenWithUser(String token, UserDetails userDetails, String userId) {
        return extractUsername(token).equals(userDetails.getUsername()) &&
                extractUserId(token).equals(userId) &&
                !isTokenExpired(token);
    }
}