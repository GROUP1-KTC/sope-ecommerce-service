package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserSettingRepository extends JpaRepository<UserSetting, UUID> {
    Optional<UserSetting> findByUserId(UUID userId);
}
