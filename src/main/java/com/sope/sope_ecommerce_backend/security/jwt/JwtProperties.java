package com.sope.sope_ecommerce_backend.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {
    private String secret;
    private Long expiration;
    private Long refreshExpiration;
}
