package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.ComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ComplaintRepository extends JpaRepository<ComplaintEntity, UUID> {

}
