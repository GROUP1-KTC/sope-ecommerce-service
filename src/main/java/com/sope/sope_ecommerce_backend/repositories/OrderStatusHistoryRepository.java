package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
}