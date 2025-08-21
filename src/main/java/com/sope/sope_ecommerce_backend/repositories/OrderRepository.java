package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.Cart;
import com.sope.sope_ecommerce_backend.entities.Order;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByAppUser(AppUser appUser);
    Optional<Order> findByIdempotencyKey(String idempotencyKey);

    Optional<Order> findByIdempotencyKeyAndAppUser(String idempotencyKey, AppUser appUser);

}
