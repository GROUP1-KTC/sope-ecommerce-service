package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.Cart;
import com.sope.sope_ecommerce_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}