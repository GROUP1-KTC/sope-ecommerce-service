package com.sope.sope_ecommerce_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sope.sope_ecommerce_backend.entities.ProductDetailEntity;
import java.util.UUID;

public interface ProductDetailRepository extends JpaRepository<ProductDetailEntity, UUID> {
}