package com.sope.sope_ecommerce_backend.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "aiService", url = "${ai-service.url:http://localhost:8000/api/v1/}")
public interface AiService {

    @PostMapping("/embedding/embed")
    Map<String, Object> getEmbedding(@RequestBody Map<String, String> body);

    @PostMapping("/sentiment/")
    Map<String, String> getSentiment(@RequestBody Map<String, String> body);

    @GetMapping("/recommend/content/product/{productId}")
    Map<String, Object> recommendContentByProductId(@PathVariable("productId") Long productId, @RequestParam("top_n") int topN);


    @PostMapping("/recommend/content/")
    Map<String, String> updateSimilarProducts(@RequestParam("top_n") int topN);


    @GetMapping("/recommend/fpgrowth/product/{productId}")
    Map<String, Object> recommendFpgrowthByProductId(@PathVariable("productId") Long productId, @RequestParam("top_n") int topN);

    @PostMapping("/recommend/fpgrowth/")
    Map<String, String> updateProductsSuggested(@RequestParam("top_n") int topN, @RequestParam("min_support") double minSupport, @RequestParam("min_confidence") double minConfidence);


    @GetMapping("/recommend/users/{userId}")
    Map<String, Object> recommendUserCF(@PathVariable("userId") String userId, @RequestParam("top_n") int topN);

    @PostMapping("/recommend/users/")
    Map<String, String> updateUserRecommendation(@RequestParam("top_n") int topN);



}
