package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.ShopIdentification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShopIdentificationRepository extends JpaRepository<ShopIdentification, UUID> {
}
