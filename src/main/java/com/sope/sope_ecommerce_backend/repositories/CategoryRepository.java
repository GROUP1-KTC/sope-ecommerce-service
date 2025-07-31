package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
      Optional<CategoryEntity> findBySlug(String slug);

}