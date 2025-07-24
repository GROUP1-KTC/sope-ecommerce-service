package com.sope.sope_ecommerce_backend.modules.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.sope.sope_ecommerce_backend.modules.product.entity.ProductDetail;
import java.util.UUID;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, UUID> {
      List<ProductDetail> findByProduct_ProductId(UUID productId);
}