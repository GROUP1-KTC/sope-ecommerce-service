package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.FlashSaleProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FlashSaleProgramRepository extends JpaRepository<FlashSaleProgram, UUID> {
  List<FlashSaleProgram> findByActiveTrue();

  List<FlashSaleProgram> findByProductVariant_ProductVariantId(UUID productVariantId);
}