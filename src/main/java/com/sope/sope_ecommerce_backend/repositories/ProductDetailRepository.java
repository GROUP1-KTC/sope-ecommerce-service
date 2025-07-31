package com.sope.sope_ecommerce_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.sope.sope_ecommerce_backend.entities.ProductDetailEntity;
import java.util.UUID;

public interface ProductDetailRepository extends JpaRepository<ProductDetailEntity, UUID> {
      List<ProductDetailEntity> findByProduct_ProductId(UUID productId);
}