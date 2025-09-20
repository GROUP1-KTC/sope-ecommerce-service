package com.sope.sope_ecommerce_backend.configuration;

import com.sope.sope_ecommerce_backend.integration.gemini.GeminiProperties;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class GeminiFeignConfig {
    @Bean
    public RequestInterceptor geminiInterceptor(GeminiProperties props) {
        return requestTemplate -> {
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Authorization", "Bearer " + props.getApiKey());
        };
    }
}
