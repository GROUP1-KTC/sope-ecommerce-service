package com.sope.sope_ecommerce_backend.integration.shipping;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "shipping-unit.goship")
@Data
public class GoshipProperties {
    private String baseUrl;
    private String authToken;
}