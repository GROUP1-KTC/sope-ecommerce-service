package com.sope.sope_ecommerce_backend.modules.product.repository;

import com.sope.sope_ecommerce_backend.modules.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
      Optional<Category> findBySlug(String slug);

}