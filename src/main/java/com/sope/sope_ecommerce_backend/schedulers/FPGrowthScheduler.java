package com.sope.sope_ecommerce_backend.schedulers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sope.sope_ecommerce_backend.client.AiService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FPGrowthScheduler {
    private final AiService aiService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void runFPGrowthAlgorithm() {
        aiService.updateProductsSuggested(null, null, null);
    }
}
