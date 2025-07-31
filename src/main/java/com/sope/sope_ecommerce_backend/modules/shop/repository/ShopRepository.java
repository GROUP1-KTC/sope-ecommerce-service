package com.sope.sope_ecommerce_backend.modules.shop.repository;

import com.sope.sope_ecommerce_backend.modules.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShopRepository extends JpaRepository<Shop, UUID> {
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}