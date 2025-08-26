package com.sope.sope_ecommerce_backend.schedulers;

import com.sope.sope_ecommerce_backend.entities.TempOrder;
import com.sope.sope_ecommerce_backend.repositories.TempOrderRepository;
import com.sope.sope_ecommerce_backend.services.ProductVariantService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class TempOrderCleanupScheduler {
    private final TempOrderRepository tempOrderRepository;
    private final ProductVariantService productVariantService;

    @Scheduled(cron = "0 */20 * * * *")
    public void cleanupExpiredTempOrders() {
        List<TempOrder> expiredOrders = tempOrderRepository.findByExpiresAtBefore(LocalDateTime.now());

        for (TempOrder temp : expiredOrders) {
            temp.getOrderItems().forEach(item -> {
                productVariantService.retrieveProductVariantStock(item.getProductVariant().getProductVariantId(), item.getQuantity());
            });

            tempOrderRepository.delete(temp);
        }
    }
}
