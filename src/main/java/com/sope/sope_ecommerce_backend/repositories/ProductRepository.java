package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sope.sope_ecommerce_backend.entities.CategoryEntity;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
      List<ProductEntity> findByCategoryIn(List<CategoryEntity> categories);

      Optional<ProductEntity> findBySlug(String slug);

}