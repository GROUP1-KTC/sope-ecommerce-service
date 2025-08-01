package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.ComplaintResolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ComplaintResolutionRepository  extends JpaRepository<ComplaintResolutionEntity, UUID> {

}
