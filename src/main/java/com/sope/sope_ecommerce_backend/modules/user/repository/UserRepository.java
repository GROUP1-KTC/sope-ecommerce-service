package com.sope.sope_ecommerce_backend.modules.user.repository;

import com.sope.sope_ecommerce_backend.modules.user.entity.Account;
import com.sope.sope_ecommerce_backend.modules.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
    User findByAccount(Account account);
}