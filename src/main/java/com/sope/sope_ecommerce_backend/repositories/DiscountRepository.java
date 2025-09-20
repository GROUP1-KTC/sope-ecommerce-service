package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.Discount;
import com.sope.sope_ecommerce_backend.enums.DiscountScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> , JpaSpecificationExecutor<Discount> {
    Optional<Discount> findByCode(String code);

    boolean existsByCode(String code);

    List<Discount> findByScopeIn(List<DiscountScope> scopes);

    List<Discount> findAllByStartDateBeforeAndEndDateAfter(LocalDateTime startDate, LocalDateTime endDate);
}
