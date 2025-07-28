package com.sope.sope_ecommerce_backend.modules.product.repository;

import com.sope.sope_ecommerce_backend.modules.product.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
}