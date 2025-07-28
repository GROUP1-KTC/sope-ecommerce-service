package com.sope.sope_ecommerce_backend.modules.product.repository;

import com.sope.sope_ecommerce_backend.modules.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sope.sope_ecommerce_backend.modules.product.entity.Category;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, UUID> {
      List<Product> findByCategoryIn(List<Category> categories);

      Optional<Product> findBySlug(String slug);

}