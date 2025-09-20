package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
      Optional<Attribute> findByNameAndValue(String name, String value);
}