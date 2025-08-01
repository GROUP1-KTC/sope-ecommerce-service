package com.sope.sope_ecommerce_backend.repositories;

import java.util.UUID;

import com.sope.sope_ecommerce_backend.entities.ReturnRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReturnRequestRepository extends JpaRepository<ReturnRequestEntity, UUID> {
}