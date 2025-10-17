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

    @PostMapping("/recommend/content/")
    Map<String, String> updateSimilarProducts(
            @RequestParam(name = "top_n", required = false, defaultValue = "10") Integer topN);

    @PostMapping("/recommend/fpgrowth/")
    Map<String, String> updateProductsSuggested(
            @RequestParam(name = "top_n", required = false, defaultValue = "10") Integer topN,
            @RequestParam(name = "min_support", required = false, defaultValue = "0.05") Double minSupport,
            @RequestParam(name = "min_confidence", required = false, defaultValue = "0.4") Double minConfidence);

    @PostMapping("/recommend/users/")
    Map<String, String> updateUserRecommendation(
            @RequestParam(name = "top_n", required = false, defaultValue = "10") Integer topN);

    @PostMapping(
            value = "/search_by_image/caption",
            consumes = {"multipart/form-data"}
    )
    Map<String, Object> getCaptionFromImage(@RequestPart("file") org.springframework.web.multipart.MultipartFile file);

}
