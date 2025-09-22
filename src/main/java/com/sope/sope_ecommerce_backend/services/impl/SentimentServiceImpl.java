package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.client.AiService;
import com.sope.sope_ecommerce_backend.enums.Sentiment;
import com.sope.sope_ecommerce_backend.services.SentimentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SentimentServiceImpl implements SentimentService {

    private final AiService aiService;

    @Override
    public Sentiment getSentiment(String comments) {
        if (comments == null || comments.isEmpty()) {
            return Sentiment.NEUTRAL;
        }

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", comments);

        try {
            Map<String, String> response = aiService.getSentiment(requestBody);
            String sentimentStr = response.get("prediction");

            if (sentimentStr == null) {
                return Sentiment.NEUTRAL;
            }

            return switch (sentimentStr.toLowerCase()) {
                case "positive" -> Sentiment.POSITIVE;
                case "negative" -> Sentiment.NEGATIVE;
                default -> Sentiment.NEUTRAL;
            };
        } catch (Exception e) {
            e.printStackTrace();
            return Sentiment.NEUTRAL;
        }
    }
}
