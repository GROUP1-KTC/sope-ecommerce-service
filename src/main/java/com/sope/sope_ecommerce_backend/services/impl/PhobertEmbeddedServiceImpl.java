package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.client.AiService;
import com.sope.sope_ecommerce_backend.services.PhobertEmbeddedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PhobertEmbeddedServiceImpl implements PhobertEmbeddedService {

    private final AiService aiService;

    @Override
    public float[] getEmbedding(String text) {
        Map<String, String> body = Map.of("text", text);
        Map<String, Object> response = aiService.getEmbedding(body);

        @SuppressWarnings("unchecked")
        List<Double> vecDouble = (List<Double>) response.get("embedding");

        float[] embedding = new float[vecDouble.size()];
        for (int i = 0; i < vecDouble.size(); i++) {
            embedding[i] = vecDouble.get(i).floatValue();
        }
        return embedding;
    }
}

