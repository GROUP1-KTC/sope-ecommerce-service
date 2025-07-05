package com.sope.sope_ecommerce_backend.modules.user.repository;


import com.sope.sope_ecommerce_backend.modules.user.entity.Account;
import com.sope.sope_ecommerce_backend.modules.user.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String userName);

    Page<Account> findAllByRole(Role role, Pageable pageable);

    Optional<Account> findById(Integer id);

    void deleteById(Integer id);
}
