package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.TempOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TempOrderRepository extends JpaRepository<TempOrder, UUID> {
    List<TempOrder> findByExpiresAtBefore(LocalDateTime dateTime);

    Optional <TempOrder> findByIdempotencyKey(String idempotencyKey);
}