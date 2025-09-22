package com.sope.sope_ecommerce_backend.schedulers;

import com.sope.sope_ecommerce_backend.entities.Order;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.repositories.OrderRepository;
import com.sope.sope_ecommerce_backend.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<Order> expiredOrders = findExpiredPendingOrders(LocalDateTime.now());
        expiredOrders.forEach(order -> orderService.cancelOrder(order.getOrderId(),
                "Order expired due to inactivity", order.getAppUser().getId()));
    }

    public List<Order> findExpiredPendingOrders(LocalDateTime now) {
        return orderRepository.findByStatusAndExpireAtBefore(OrderStatus.PENDING, now);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateCompletedOrders() {

        List<Order> ordersToComplete = orderRepository.findByStatus(OrderStatus.DELIVERED);

        for (Order order : ordersToComplete) {
            order.setStatus(OrderStatus.COMPLETED);
        }

        orderRepository.saveAll(ordersToComplete);
    }

}
