package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, UUID> {
      List<ProductVariantEntity> findByProduct_ProductId(UUID productId);

}