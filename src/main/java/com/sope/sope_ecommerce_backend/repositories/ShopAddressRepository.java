package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.entities.ShopAddress;
import com.sope.sope_ecommerce_backend.entities.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShopAddressRepository extends JpaRepository<ShopAddress, UUID> {

    Optional<ShopAddress> findByShop(Shop shop);

    void deleteByShop(Shop shop);
}

