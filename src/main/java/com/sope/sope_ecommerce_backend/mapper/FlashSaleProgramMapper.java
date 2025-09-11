// FlashSaleProgramMapper.java
package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.FlashSaleProgramRequest;
import com.sope.sope_ecommerce_backend.dto.response.FlashSaleProgramDTO;
import com.sope.sope_ecommerce_backend.entities.FlashSaleProgram;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FlashSaleProgramMapper {
  @Mapping(target = "productVariantId", source = "productVariant.productVariantId")
  FlashSaleProgramDTO toDto(FlashSaleProgram entity);

  @Mapping(target = "productVariant", ignore = true)
  @Mapping(target = "active", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  FlashSaleProgram toEntity(FlashSaleProgramRequest request);
}
