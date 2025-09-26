package com.sope.sope_ecommerce_backend.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sope.sope_ecommerce_backend.entities.ReviewEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {
      Optional<ReviewEntity> findByAppUser_IdAndProductVariant_ProductVariantId(UUID userId, UUID productVariantId);

      List<ReviewEntity> findByProductVariant_Product_ProductId(UUID productId);

      @Query("SELECT r.content FROM ReviewEntity r WHERE r.productVariant.product.productId = :productId")
      List<String> findAllContentsByProductId(@Param("productId") UUID productId);

      List<ReviewEntity> findByAppUserId(UUID userId);

}
