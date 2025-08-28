package com.sope.sope_ecommerce_backend.configuration;

import com.sope.sope_ecommerce_backend.integration.shipping.GoshipProperties;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor goshipInterceptor(GoshipProperties props) {
        return requestTemplate -> {
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Authorization", "Bearer " + props.getAuthToken());
        };
    }
}