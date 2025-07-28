package com.sope.sope_ecommerce_backend.modules.user.repository;

import com.sope.sope_ecommerce_backend.modules.user.entity.Shop;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ShopRepository extends JpaRepository<Shop, UUID> {

}
