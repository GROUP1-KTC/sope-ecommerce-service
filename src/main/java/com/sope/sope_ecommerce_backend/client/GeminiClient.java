package com.sope.sope_ecommerce_backend.client;

import com.sope.sope_ecommerce_backend.configuration.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "geminiClient", url = "https://generativelanguage.googleapis.com", configuration = FeignConfig.class)
public interface GeminiClient {

    @PostMapping("/v1beta/openai/chat/completions")
    Map<String, Object> sendChat(@RequestBody Map<String, Object> body);

}


