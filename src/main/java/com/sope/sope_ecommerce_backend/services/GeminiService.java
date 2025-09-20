package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.ChatRequest;
import com.sope.sope_ecommerce_backend.dto.response.ChatAIResponse;

import java.util.Map;
import java.util.UUID;

public interface GeminiService {
    public ChatAIResponse sendMessage(ChatRequest request);

    public Map<String, Object> validateProduct(String name, String description);

    String generateAndSaveOverallReview(UUID productId);
}
