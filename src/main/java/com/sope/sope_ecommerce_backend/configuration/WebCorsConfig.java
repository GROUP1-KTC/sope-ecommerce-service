package com.sope.sope_ecommerce_backend.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
@Data
@Configuration
@ConfigurationProperties(prefix = "web.cors")
public class WebCorsConfig {

    private List<String> allowedOrigins;
    private List<String> allowedHeaders;
    private List<String> exposedHeaders;
    private List<String> allowedMethods;
    private Boolean allowedCredentials;
    private Long maxAge;

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(allowedOrigins);
        corsConfig.setAllowedMethods(allowedMethods);
        corsConfig.setAllowedHeaders(allowedHeaders);
        corsConfig.setAllowCredentials(allowedCredentials);
        corsConfig.setExposedHeaders(exposedHeaders);
        corsConfig.setMaxAge(maxAge);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
