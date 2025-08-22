package com.sope.sope_ecommerce_backend.schedulers;

import com.sope.sope_ecommerce_backend.dto.request.UpdateOrderStatusRequest;
import com.sope.sope_ecommerce_backend.entities.Order;
import com.sope.sope_ecommerce_backend.entities.OrderItem;
import com.sope.sope_ecommerce_backend.entities.Product;
import com.sope.sope_ecommerce_backend.entities.ProductVariant;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.services.OrderService;
import com.sope.sope_ecommerce_backend.services.ProductVariantService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderCleanupService {

    private final OrderService orderService;

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void cancelExpiredOrders() {
        List<Order> expiredOrders = orderService.findOrderByStatusPendingAndExpireAtBefore(LocalDateTime.now());
        expiredOrders.forEach(order -> orderService.cancelOrder(order.getOrderId()));
    }

}
