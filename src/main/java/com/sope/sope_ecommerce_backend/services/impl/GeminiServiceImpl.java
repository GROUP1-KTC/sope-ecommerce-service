//package com.sope.sope_ecommerce_backend.services.impl;
//
//import com.sope.sope_ecommerce_backend.dto.request.ChatRequest;
//import com.sope.sope_ecommerce_backend.dto.response.ChatAIResponse;
//import com.sope.sope_ecommerce_backend.entities.Product;
//import com.sope.sope_ecommerce_backend.repositories.ProductRepository;
//import com.sope.sope_ecommerce_backend.services.GeminiService;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.ai.chat.client.ChatClient;
//
//import java.util.Comparator;
//import java.util.List;
//
//@AllArgsConstructor
//@Service
//public class GeminiServiceImpl implements GeminiService {
//
//    private final ChatClient chatClient;
//    private final ProductRepository productRepository;
//
////    public ChatAIResponse sendMessage(ChatRequest request) {
////        List<Product> products = productRepository.findAll();
////
////        if (products.isEmpty()) {
////            return new ChatAIResponse("Hiện tại chưa có sản phẩm nào trong hệ thống.");
////        }
////
////        // Ví dụ: xử lý nhanh 1 số keyword trước khi gọi AI
////        String msg = request.message().toLowerCase();
////        if (msg.contains("rẻ nhất")) {
////            Product cheapest = products.stream()
////                    .min(Comparator.comparing(Product::getPrice))
////                    .orElseThrow();
////            return new ChatAIResponse("Sản phẩm rẻ nhất là: " + cheapest.getName()
////                    + " - giá " + cheapest.getPrice());
////        }
////
////        if (msg.contains("đắt nhất")) {
////            Product expensive = products.stream()
////                    .max(Comparator.comparing(Product::getPrice))
////                    .orElseThrow();
////            return new ChatAIResponse("Sản phẩm đắt nhất là: " + expensive.getName()
////                    + " - giá " + expensive.getPrice());
////        }
////
////        // Ghép data sản phẩm thành context cho AI
////        String productInfo = products.stream()
////                .map(p -> String.format("%s (%s): %s - giá %s",
////                        p.getName(), p.getCategory(), p.getDescription(), p.getPrice()))
////                .collect(Collectors.joining("\n"));
////
////        String prompt = """
////            Bạn là chatbot tư vấn sản phẩm.
////            Dữ liệu sản phẩm hiện có:
////            %s
////
////            Người dùng hỏi: %s
////            Trả lời gọn gàng, dễ hiểu, ưu tiên chọn sản phẩm trong danh sách.
////            """.formatted(productInfo, request.message());
////
////        String reply = chatClient.prompt()
////                .user(prompt)
////                .call()
////                .content();
////
////        return new ChatAIResponse(reply);
////    }
//
//}
