// AdsProgramMapper.java
package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.AdsProgramRequest;
import com.sope.sope_ecommerce_backend.dto.response.AdsProgramDTO;
import com.sope.sope_ecommerce_backend.entities.AdsProgram;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AdsProgramMapper {
  @Mapping(target = "productInfo", source = "product")
  AdsProgramDTO toDto(AdsProgram entity);

  @Mapping(target = "product", ignore = true)
  @Mapping(target = "endDate", ignore = true)
  @Mapping(target = "active", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "dailyFee", ignore = true)
  @Mapping(target = "id", ignore = true)
  AdsProgram toEntity(AdsProgramRequest request);
}
