// package com.sope.sope_ecommerce_backend.repositories;

// import java.util.UUID;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

// import com.sope.sope_ecommerce_backend.entities.OrderItem;
// import com.sope.sope_ecommerce_backend.enums.OrderStatus;

// public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

// @Query("""
// SELECT CASE WHEN COUNT(oi) > 0 THEN true ELSE false END
// FROM OrderItem oi
// WHERE oi.order.appUser.id = :userId
// AND oi.order.status = :status
// AND oi.productVariant.id = :productVariantId
// """)
// boolean existsByUserAndStatusAndProductVariant(
// @Param("userId") UUID userId,
// @Param("status") OrderStatus status,
// @Param("productVariantId") UUID productVariantId);
// }