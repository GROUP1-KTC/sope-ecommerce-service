package com.sope.sope_ecommerce_backend.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
                "Extract the important keywords from the following question. " +
                        "Return **only** a JSON array of strings, without any extra explanation, text, or formatting:\n" +
                        userQuestion
        );


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
            return new ChatAIResponse("Không tìm thấy sản phẩm nào phù hợp với yêu cầu của bạn");
        }

        // ==============================
        // 3. Build context cho Gemini
        // ==============================
        StringBuilder context = new StringBuilder();
        context.append("You are Chatbot AI for an e-commerce store.\n");
        context.append("Your role is to answer customer questions directly, clearly, and politely. ")
                .append("Do NOT include explanations, reasoning, or extra commentary.\n\n");
        context.append("Customer asks: ").append(userQuestion).append("\n\n");
        context.append("Relevant products in inventory:\n");
        for (Product p : candidates) {
            context.append("- ").append(p.getName())
                    .append(": ").append(p.getDescription() != null ? p.getDescription() : "")
                    .append("\n");
        }
        context.append("\nPlease respond to the customer directly based on the products above.");


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

    @Override
    public Map<String, Object> validateProduct(String name, String description) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a strict content validator for an e-commerce platform.\n")
                .append("Your task is to check if the product name and description are legal, ")
                .append("appropriate, and do not violate laws or policies.\n\n")
                .append("Product name: ").append(name).append("\n")
                .append("Product description: ").append(description).append("\n\n")
                .append("Return ONLY a valid JSON object in one of these two forms:\n")
                .append("{\"valid\": true}\n")
                .append("OR\n")
                .append("{\"valid\": false, \"reason\": \"<reason>\"}\n\n")
                .append("Do not add extra text, markdown, or explanation outside the JSON.");

        // Chuẩn bị request
        Map<String, Object> msg = new HashMap<>();
        msg.put("role", "user");
        msg.put("content", prompt.toString());

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gemini-2.0-flash");
        body.put("messages", List.of(msg));

        Map<String, Object> resp = geminiClient.sendChat(body);

        // Parse response
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) resp.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> first = choices.get(0);
                Map<String, Object> messageResp = (Map<String, Object>) first.get("message");
                String content = messageResp.get("content").toString().trim();

                // Đảm bảo chỉ lấy JSON
                if (content.startsWith("```")) {
                    content = content.replaceAll("(?s)```json", "")
                            .replaceAll("```", "").trim();
                }

                // Parse sang Map
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(content, Map.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Nếu có lỗi thì default trả false
        return Map.of(
                "valid", false,
                "reason", "Không thể xác thực sản phẩm do lỗi hệ thống"
        );
    }

}
