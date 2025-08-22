
package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);

    @Query("SELECT ur.role.roleName FROM UserRole ur WHERE ur.user.id = :userId")
    List<String> findRoleNamesByUserId(UUID userId);

}

