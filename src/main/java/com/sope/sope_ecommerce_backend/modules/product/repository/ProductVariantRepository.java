package com.sope.sope_ecommerce_backend.modules.product.repository;

import com.sope.sope_ecommerce_backend.modules.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {
      List<ProductVariant> findByProduct_ProductId(UUID productId);

}