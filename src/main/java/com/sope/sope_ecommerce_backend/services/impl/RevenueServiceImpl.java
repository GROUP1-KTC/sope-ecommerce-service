package com.sope.sope_ecommerce_backend.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sope.sope_ecommerce_backend.dto.response.RevenueResponse;
import com.sope.sope_ecommerce_backend.entities.Order;
import com.sope.sope_ecommerce_backend.entities.OrderItem;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.repositories.OrderRepository;
import com.sope.sope_ecommerce_backend.services.RevenueService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {
      private final OrderRepository orderRepository;

      // Doanh thu của 1 shop (seller)
      @Override
      @Transactional(readOnly = true)
      public RevenueResponse calculateShopRevenue(UUID shopId) {
            List<Order> completedOrders = orderRepository.findByStatus(OrderStatus.DELIVERED);

            BigDecimal shopRevenue = BigDecimal.ZERO;

            for (Order order : completedOrders) {
                  // lọc theo shopId (giả sử ProductVariant có getShop().getId())
                  for (OrderItem item : order.getOrderItems()) {
                        UUID sellerId = item.getProductVariant().getProduct().getShop().getId(); // giả sử entity có
                                                                                                 // quan hệ này

                        if (sellerId.equals(shopId)) {
                              BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

                              BigDecimal commission = itemTotal
                                            .multiply(item.getProductVariant().getProduct().getCategory().getCommissionFeePercent())

                                          .divide(BigDecimal.valueOf(100));

                              BigDecimal sellerRevenue = itemTotal.subtract(commission);

                              shopRevenue = shopRevenue.add(sellerRevenue);
                        }
                  }
            }

            return new RevenueResponse(shopRevenue);
      }

      // Doanh thu admin (hoa hồng từ tất cả đơn hàng completed)
      @Override
      @Transactional(readOnly = true)
      public RevenueResponse calculateAdminRevenue() {
            List<Order> completedOrders = orderRepository.findByStatus(OrderStatus.DELIVERED);

            BigDecimal totalCommission = BigDecimal.ZERO;

            for (Order order : completedOrders) {
                  for (OrderItem item : order.getOrderItems()) {
                        BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

                        BigDecimal commission = itemTotal
                                    .multiply(item.getProductVariant().getProduct().getCategory().getCommissionFeePercent())
                                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                        totalCommission = totalCommission.add(commission);
                  }
            }

            return new RevenueResponse(totalCommission);
      }
}
