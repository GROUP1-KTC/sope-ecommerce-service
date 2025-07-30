package com.sope.sope_ecommerce_backend.modules.user.repository;


import com.sope.sope_ecommerce_backend.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
