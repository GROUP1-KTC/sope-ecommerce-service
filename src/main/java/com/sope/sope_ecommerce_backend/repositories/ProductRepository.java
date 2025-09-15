package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
      Optional<Product> findBySlug(String slug);

      List<Product> findByCategoryIdIn(Collection<UUID> categoryIds);

      Page<Product> findByShopId(UUID shopId, Pageable pageable);

      Optional<Product> findByName(String name);

      @Query(value = """
          SELECT * FROM products 
          ORDER BY (embedding <=> cast(:queryVector as vector)) 
          LIMIT :limit
          """, nativeQuery = true)
      List<Product> findMostSimilar(@Param("queryVector") float[] queryVector, @Param("limit") int limit);

      @Query(value = """
    SELECT * FROM products
    WHERE LOWER(name) LIKE ANY(:patterns)
       OR LOWER(description) LIKE ANY(:patterns)
    LIMIT :limit
    """, nativeQuery = true)
      List<Product> searchByKeywords(@Param("patterns") String[] patterns, @Param("limit") int limit);


}