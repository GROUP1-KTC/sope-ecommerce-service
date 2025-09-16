package com.sope.sope_ecommerce_backend.configuration;

import com.sope.sope_ecommerce_backend.integration.gemini.GeminiProperties;
import com.sope.sope_ecommerce_backend.integration.shipping.GoshipProperties;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class GoShipFeignConfig {

    @Bean
    public RequestInterceptor goshipInterceptor(GoshipProperties props) {
        return requestTemplate -> {
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Authorization", "Bearer " + props.getAuthToken());
        };
    }

}