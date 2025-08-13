package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.ProductEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
      Optional<ProductEntity> findBySlug(String slug);

}