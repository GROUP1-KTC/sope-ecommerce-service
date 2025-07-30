package com.sope.sope_ecommerce_backend.modules.address.repository;

import com.sope.sope_ecommerce_backend.modules.address.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

}