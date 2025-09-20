package com.sope.sope_ecommerce_backend.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sope.sope_ecommerce_backend.entities.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {
      Optional<ReviewEntity> findByAppUser_IdAndProductVariant_ProductVariantId(UUID userId, UUID productVariantId);

      List<ReviewEntity> findByProductVariant_Product_ProductId(UUID productId);

}
