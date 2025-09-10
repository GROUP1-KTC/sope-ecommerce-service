package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.AdsProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AdsProgramRepository extends JpaRepository<AdsProgram, UUID> {
  List<AdsProgram> findByActiveTrue();

  List<AdsProgram> findByProduct_ProductId(UUID productId);
}
