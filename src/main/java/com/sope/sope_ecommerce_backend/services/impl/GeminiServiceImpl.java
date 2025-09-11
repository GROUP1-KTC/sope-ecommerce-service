package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.client.GeminiClient;
import com.sope.sope_ecommerce_backend.dto.request.ChatRequest;
import com.sope.sope_ecommerce_backend.dto.response.ChatAIResponse;
import com.sope.sope_ecommerce_backend.entities.Product;
import com.sope.sope_ecommerce_backend.repositories.ProductRepository;
import com.sope.sope_ecommerce_backend.services.GeminiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GeminiServiceImpl implements GeminiService {

    private final GeminiClient geminiClient;
    private final ProductRepository productRepository;

    @Override
    public ChatAIResponse sendMessage(ChatRequest request) {
        String userQuestion = request.message();

        // ==============================
        // 1. Gọi Gemini để extract keywords
        // ==============================
        Map<String, Object> keywordMsg = new HashMap<>();
        keywordMsg.put("role", "user");
        keywordMsg.put("content",
                "Extract important keywords from this question, return as a JSON array of strings only:\n" + userQuestion);

        Map<String, Object> keywordBody = new HashMap<>();
        keywordBody.put("model", "gemini-2.0-flash");
        keywordBody.put("messages", List.of(keywordMsg));

        Map<String, Object> keywordResp = geminiClient.sendChat(keywordBody);

        List<String> keywords = new ArrayList<>();
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) keywordResp.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> first = choices.get(0);
                Map<String, Object> messageResp = (Map<String, Object>) first.get("message");
                String content = messageResp.get("content").toString();

                // parse thủ công (có thể thay bằng Jackson)
                // parse thủ công (có thể thay bằng Jackson)
                content = content
                        .replaceAll("(?s)```json", "") // bỏ code fence mở
                        .replaceAll("```", "")         // bỏ code fence đóng
                        .replaceAll("[\\[\\]\"]", ""); // bỏ [ ], "
                keywords = Arrays.stream(content.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (keywords.isEmpty()) {
            return new ChatAIResponse("Xin lỗi, mình không tìm thấy từ khóa phù hợp.");
        }

        // ==============================
        // 2. Query DB theo keywords
        // ==============================
        String[] patterns = keywords.stream()
                .map(k -> "%" + k.toLowerCase() + "%")
                .toArray(String[]::new);

        List<Product> candidates = productRepository.searchByKeywords(patterns, 50);

        if (candidates.isEmpty()) {
            return new ChatAIResponse("Không tìm thấy sản phẩm nào phù hợp với từ khóa: " + keywords);
        }

        // ==============================
        // 3. Build context cho Gemini
        // ==============================
        StringBuilder context = new StringBuilder("Người dùng hỏi: " + userQuestion + "\n");
        context.append("Các sản phẩm phù hợp trong kho dữ liệu:\n");
        for (Product p : candidates) {
            context.append("- ").append(p.getName())
                    .append(": ").append(p.getDescription() != null ? p.getDescription() : "")
                    .append("\n");
        }

        // ==============================
        // 4. Gọi Gemini để tạo câu trả lời cuối
        // ==============================
        Map<String, Object> finalMsg = new HashMap<>();
        finalMsg.put("role", "user");
        finalMsg.put("content", context.toString());

        Map<String, Object> finalBody = new HashMap<>();
        finalBody.put("model", "gemini-2.0-flash");
        finalBody.put("messages", List.of(finalMsg));

        Map<String, Object> finalResp = geminiClient.sendChat(finalBody);

        String reply = "Không nhận được phản hồi";
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) finalResp.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> first = choices.get(0);
                Map<String, Object> messageResp = (Map<String, Object>) first.get("message");
                reply = messageResp.get("content").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ChatAIResponse(reply);
    }
}
