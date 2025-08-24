package com.sope.sope_ecommerce_backend.schedulers;

import com.sope.sope_ecommerce_backend.dto.request.UpdateOrderStatusRequest;
import com.sope.sope_ecommerce_backend.entities.Order;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.repositories.OrderRepository;
import com.sope.sope_ecommerce_backend.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderScheduler {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void cancelExpiredOrders() {
        List<Order> expiredOrders = orderService.findOrderByStatusPendingAndExpireAtBefore(LocalDateTime.now());
        expiredOrders.forEach(order -> orderService.cancelOrder(order.getOrderId(),
                "Order expired due to inactivity", order.getAppUser().getId()));
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateCompletedOrders() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime thresholdDate = todayStart.minusDays(14);


        List<Order> ordersToComplete = orderRepository.findByStatusAndDeliveryDateBefore(OrderStatus.DELIVERED, thresholdDate);
        for (Order order : ordersToComplete) {
            order.setStatus(OrderStatus.COMPLETED);
        }

        orderRepository.saveAll(ordersToComplete);
    }

}
