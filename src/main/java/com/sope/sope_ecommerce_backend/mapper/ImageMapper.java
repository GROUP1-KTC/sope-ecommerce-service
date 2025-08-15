package com.sope.sope_ecommerce_backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sope.sope_ecommerce_backend.dto.response.ImageDTO;
import com.sope.sope_ecommerce_backend.entities.ImageEntity;

@Mapper(componentModel = "spring")
public interface ImageMapper {
      @Mapping(target = "product", ignore = true)
      ImageEntity toEntity(ImageDTO dto);

      ImageDTO toDto(ImageEntity entity);
}
