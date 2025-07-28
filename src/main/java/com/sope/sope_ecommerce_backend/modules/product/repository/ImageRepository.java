package com.sope.sope_ecommerce_backend.modules.product.repository;

import com.sope.sope_ecommerce_backend.modules.product.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}