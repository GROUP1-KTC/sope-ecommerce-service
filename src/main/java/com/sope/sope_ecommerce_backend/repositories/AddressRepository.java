package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.Address;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findByAppUser_Id(UUID userId);
    Optional<Address> findByAppUser_IdAndIsDefaultTrue(UUID userId);
    List<Address> findByAppUser(AppUser appUser);
    List<Address> findByAppUserAndIsDefault(AppUser user, boolean isDefault);

}