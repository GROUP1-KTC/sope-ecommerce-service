package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, UUID> {
    List<PaymentCard> findByAppUser_Id(UUID userId);
}
