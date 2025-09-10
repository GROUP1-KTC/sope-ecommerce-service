package com.sope.sope_ecommerce_backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sope.sope_ecommerce_backend.entities.OrderItem;
import com.sope.sope_ecommerce_backend.entities.OrderItemId;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

      boolean existsByOrder_AppUser_IdAndOrder_StatusInAndProductVariant_ProductVariantId(
                  UUID userId, List<OrderStatus> statuses, UUID productVariantId);
}
