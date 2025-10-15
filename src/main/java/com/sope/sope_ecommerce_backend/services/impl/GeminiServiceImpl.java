package com.sope.sope_ecommerce_backend.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sope.sope_ecommerce_backend.client.GeminiClient;
import com.sope.sope_ecommerce_backend.dto.request.ChatRequest;
import com.sope.sope_ecommerce_backend.dto.response.ChatAIResponse;
import com.sope.sope_ecommerce_backend.entities.Product;
import com.sope.sope_ecommerce_backend.mapper.ProductMapper;
import com.sope.sope_ecommerce_backend.repositories.ProductRepository;
import com.sope.sope_ecommerce_backend.repositories.ReviewRepository;
import com.sope.sope_ecommerce_backend.services.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GeminiServiceImpl implements GeminiService {

    private final GeminiClient geminiClient;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ProductMapper productMapper;

    @Value("${app.base-url}")
    private String baseUrl;

    private List<String> extractKeywords(String userQuestion) {
        Map<String, Object> keywordMsg = new HashMap<>();
        keywordMsg.put("role", "user");
        keywordMsg.put("content",
                "Extract important keywords AND related terms or synonyms from the following question. " +
                        "Return **only** a JSON array of strings. Do NOT add extra explanation.\n" +
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

                // Loại bỏ code fence và ký tự dư thừa
                content = content
                        .replaceAll("(?s)```json", "")
                        .replaceAll("```", "")
                        .replaceAll("[\\[\\]\"]", "");
                keywords = Arrays.stream(content.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keywords;
    }


    @Override
    public ChatAIResponse sendMessage(ChatRequest request) {
        String userQuestion = request.message();

        List<String> keywords = extractKeywords(userQuestion);

        if (keywords.isEmpty()) {
            return new ChatAIResponse("Xin lỗi, mình không tìm thấy từ khóa phù hợp.", null);
        }

        String[] patterns = keywords.stream()
                .map(k -> "%" + k.toLowerCase() + "%")
                .toArray(String[]::new);

        List<Product> candidates = productRepository.searchByKeywords(patterns, 50);

        if (candidates.isEmpty()) {
            return new ChatAIResponse("Không tìm thấy sản phẩm nào phù hợp với yêu cầu của bạn", null);
        }

        boolean isCompare = keywords.stream()
                .anyMatch(k -> k.toLowerCase().contains("so sánh") || k.toLowerCase().contains("compare"));

        if (isCompare) {
            return buildComparisonResponse(userQuestion, candidates);
        } else {
            return buildProductListResponse(userQuestion, candidates);
        }
    }

    private ChatAIResponse buildComparisonResponse(String userQuestion, List<Product> candidates) {
        List<Product> compareProducts = candidates.stream()
                .filter(p -> userQuestion.toLowerCase().contains(p.getName().toLowerCase()))
                .limit(2)
                .collect(Collectors.toList());

        if (compareProducts.size() < 2) {
            return new ChatAIResponse("Không tìm thấy sản phẩm bạn muốn so sánh.", null);
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("Bạn là một chatbot thân thiện cho cửa hàng thương mại điện tử.\n")
                .append("Nhiệm vụ của bạn là **so sánh hai sản phẩm dưới đây dựa hoàn toàn trên thông tin mà tôi cung cấp**.\n")
                .append("Chỉ sử dụng thông tin trong dữ liệu sản phẩm, không tự thêm thông tin từ internet.\n")
                .append("Liệt kê so sánh dưới dạng các điểm nổi bật (bullet points), tránh sử dụng bảng hay Markdown.\n\n");

        for (Product p : compareProducts) {
            prompt.append("- Tên: ").append(p.getName()).append("\n")
                    .append("  Mô tả: ").append(p.getDescription()).append("\n");
        }

        prompt.append("Câu hỏi của khách hàng: ").append(userQuestion).append("\n")
                .append("Hãy trả lời trực tiếp với so sánh chi tiết, thân thiện và hữu ích, bằng các điểm bullet.");

        return callGemini(prompt.toString(), null);
    }

    private ChatAIResponse buildProductListResponse(String userQuestion, List<Product> candidates) {
        StringBuilder context = new StringBuilder();
        context.append("You are a friendly and polite chatbot for an e-commerce store.\n");
        context.append("Answer clearly and warmly, suggest relevant products, and encourage browsing.\n");
        context.append("Answer in ONE short friendly paragraph (3–4 sentences max).\n");
        context.append("Do NOT use bullet points, asterisks (*), or markdown (**).\n");
        context.append("Do NOT ask questions back. Keep the tone positive.\n\n");
        context.append("Customer asks: ").append(userQuestion).append("\n\n");

        context.append("Relevant products in inventory:\n");
        for (Product p : candidates) {
            var summary = productMapper.toProductSummaryResponse(p);
            context.append("- ").append(p.getName())
                    .append(" | Price from: ").append(summary.minPrice()).append(" VND")
                    .append(" (").append(baseUrl).append("/product-detail/").append(p.getSlug())
                    .append(" | Image: ").append(p.getDefaultImage()).append("\n");
        }

        context.append("Return ONLY a valid JSON object in EXACTLY this format.\n")
                .append("Do NOT include ```json, ``` or any extra explanation.\n")
                .append("Do NOT include any text outside the JSON.\n")
                .append("Format:\n")
                .append("{\n")
                .append("  \"content\": \"<friendly answer text>\",\n")
                .append("  \"products\": [\n")
                .append("    {\"name\": \"<product name>\", \"link\": \"<url>\", \"image\": \"<image url>\", \"minPrice\": 12345}\n")
                .append("  ]\n")
                .append("}\n");

        String link = candidates.isEmpty() ? null :
                baseUrl + "/product-detail/" + candidates.get(0).getSlug();

        return callGemini(context.toString(), link);
    }


    private ChatAIResponse callGemini(String prompt, String link) {
        Map<String, Object> finalMsg = new HashMap<>();
        finalMsg.put("role", "user");
        finalMsg.put("content", prompt);

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

        return new ChatAIResponse(reply, link);
    }


    @Override
    public Map<String, Object> validateProduct(String name, String description) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Bạn là một bộ kiểm duyệt nội dung nghiêm ngặt cho nền tảng thương mại điện tử.\n")
                .append("Nhiệm vụ của bạn là kiểm tra xem tên sản phẩm và mô tả có hợp pháp, ")
                .append("phù hợp, và không vi phạm luật hoặc chính sách hay không.\n\n")
                .append("Tên sản phẩm: ").append(name).append("\n")
                .append("Mô tả sản phẩm: ").append(description).append("\n\n")
                .append("Chỉ trả về một đối tượng JSON hợp lệ theo một trong hai dạng sau:\n")
                .append("{\"valid\": true}\n")
                .append("HOẶC\n")
                .append("{\"valid\": false, \"reason\": \"<lý do>\"}\n\n")
                .append("Không thêm văn bản, markdown hay giải thích nào ngoài JSON.");

        Map<String, Object> msg = new HashMap<>();
        msg.put("role", "user");
        msg.put("content", prompt.toString());

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gemini-2.0-flash");
        body.put("messages", List.of(msg));

        Map<String, Object> resp = geminiClient.sendChat(body);

        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) resp.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> first = choices.get(0);
                Map<String, Object> messageResp = (Map<String, Object>) first.get("message");
                String content = messageResp.get("content").toString().trim();

                if (content.startsWith("```")) {
                    content = content.replaceAll("(?s)```json", "")
                            .replaceAll("```", "").trim();
                }

                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(content, Map.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Map.of(
                "valid", false,
                "reason", "Không thể xác thực sản phẩm do lỗi hệ thống"
        );
    }

    public String generateAndSaveOverallReview(UUID productId) {
        List<String> reviewTexts = reviewRepository.findAllContentsByProductId(productId);

        if (reviewTexts.isEmpty()) {
            return "Chưa có review nào cho sản phẩm này.";
        }

        String summary = summarizeReviews(productId.toString(), reviewTexts);

        productRepository.findById(productId).ifPresent(product -> {
            product.setOverallReview(summary);
            productRepository.save(product);
        });

        return summary;
    }

    private String summarizeReviews(String productName, List<String> reviews) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Dựa trên các review dưới đây về sản phẩm, hãy viết một đoạn văn duy nhất bằng tiếng Việt tóm tắt đánh giá tổng quan về sản phẩm. " +
                        "Không phân tách ưu nhược điểm, không dùng ký hiệu, gạch đầu dòng hay Markdown, chỉ một đoạn văn liên tục.\n\n")
                .append("Danh sách review:\n");
        for (String r : reviews) {
            prompt.append("- ").append(r).append("\n");
        }

        Map<String, Object> msg = new HashMap<>();
        msg.put("role", "user");
        msg.put("content", prompt.toString());

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gemini-2.0-flash");
        body.put("messages", List.of(msg));

        Map<String, Object> resp = geminiClient.sendChat(body);

        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) resp.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> first = choices.get(0);
                Map<String, Object> messageResp = (Map<String, Object>) first.get("message");
                String content = messageResp.get("content").toString().trim();

                if (content.startsWith("```")) {
                    content = content.replaceAll("(?s)```json", "")
                            .replaceAll("```", "").trim();
                }
                return content;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Không thể tổng hợp review do lỗi hệ thống.";
    }

    public List<String> extractVietnameseKeywordsFromCaption(String englishCaption) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("role", "user");
        msg.put("content",
                "You are an expert bilingual translator and keyword extractor for e-commerce search.\n" +
                        "Given the following English caption describing a product, do these steps:\n" +
                        "1. Understand the product meaning.\n" +
                        "2. Generate **5 short Vietnamese keywords** for searching this product in an e-commerce store.\n" +
                        "   - 1–2 keywords should be specific (e.g. describe product type or color).\n" +
                        "   - The remaining should be more general or common search terms.\n" +
                        "   - Avoid overly detailed or repetitive phrases.\n" +
                        "3. Return **only** a valid JSON array of Vietnamese keywords (no markdown, no explanation).\n\n" +
                        "Caption: \"" + englishCaption + "\""
        );

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gemini-2.0-flash");
        body.put("messages", List.of(msg));

        Map<String, Object> resp = geminiClient.sendChat(body);

        List<String> keywords = new ArrayList<>();
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) resp.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> first = choices.get(0);
                Map<String, Object> messageResp = (Map<String, Object>) first.get("message");
                String content = messageResp.get("content").toString().trim();

                // Xử lý chuỗi JSON hợp lệ
                content = content
                        .replaceAll("(?s)```json", "")
                        .replaceAll("```", "")
                        .trim();

                ObjectMapper mapper = new ObjectMapper();
                keywords = mapper.readValue(content, List.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keywords;
    }


}
