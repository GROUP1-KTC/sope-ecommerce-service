package com.sope.sope_ecommerce_backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sope.sope_ecommerce_backend.entities.OrderItem;
import com.sope.sope_ecommerce_backend.entities.OrderItemId;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

      @Query("""
                  SELECT CASE WHEN COUNT(oi) > 0 THEN true ELSE false END
                  FROM OrderItem oi
                  WHERE oi.order.appUser.id = :userId
                  AND oi.order.status IN :statuses
                  AND oi.productVariant.product.productId = :productId
                  """)
      boolean existsByUserAndProductAndStatuses(
                  @Param("userId") UUID userId,
                  @Param("productId") UUID productId,
                  @Param("statuses") List<OrderStatus> statuses);

      boolean existsByOrder_AppUser_IdAndOrder_StatusInAndProductVariant_Product_ProductId(
                  UUID userId, List<OrderStatus> statuses, UUID productId);
}
