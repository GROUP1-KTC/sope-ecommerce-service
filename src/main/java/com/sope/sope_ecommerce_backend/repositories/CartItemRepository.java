package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.Cart;
import com.sope.sope_ecommerce_backend.entities.CartItem;
import com.sope.sope_ecommerce_backend.entities.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID>, JpaSpecificationExecutor<CartItem> {
    Optional<CartItem> findByCartAndProductVariant(Cart cart, ProductVariant productVariant);


}
