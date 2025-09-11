package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.ChatRequest;
import com.sope.sope_ecommerce_backend.dto.response.ChatAIResponse;

public interface GeminiService {
    public ChatAIResponse sendMessage(ChatRequest request);

}
