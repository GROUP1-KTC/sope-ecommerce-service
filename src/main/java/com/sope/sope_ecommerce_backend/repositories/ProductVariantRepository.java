package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {
      List<ProductVariant> findByProduct_ProductId(UUID productId);

}