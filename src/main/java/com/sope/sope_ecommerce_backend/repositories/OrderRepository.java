package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.Order;
import com.sope.sope_ecommerce_backend.entities.Payment;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByAppUser(AppUser appUser);

    Optional<Order> findByIdempotencyKey(String idempotencyKey);

    List<Order> findByPayment(Payment payment);

    Optional<Order> findByIdempotencyKeyAndAppUser(String idempotencyKey, AppUser appUser);

    List<Order> findAllByIdempotencyKeyContaining(String pattern);

    @Modifying
    @Query("UPDATE Order o SET o.status = 'CANCELLED' WHERE o.status = 'PENDING' AND o.expireAt < :now")
    void cancelExpiredOrders(@Param("now") LocalDateTime now);

    List<Order> findByStatusAndExpireAtBefore(OrderStatus status, LocalDateTime now);

    List<Order> findByStatus(OrderStatus status);

    Page<Order> findByShop_Id(UUID shopId, Pageable pageable);

    Page<Order> findByShop_IdAndStatus(UUID shopId, OrderStatus status, Pageable pageable);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByShop_Id(UUID shopId);
}
