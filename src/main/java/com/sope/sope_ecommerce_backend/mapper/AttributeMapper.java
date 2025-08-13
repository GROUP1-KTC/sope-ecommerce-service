package com.sope.sope_ecommerce_backend.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sope.sope_ecommerce_backend.dto.response.AttributeDTO;
import com.sope.sope_ecommerce_backend.entities.AttributeEntity;

@Mapper(componentModel = "spring")
public interface AttributeMapper {
      @Mapping(target = "attributeId", ignore = true)
      AttributeEntity toEntity(AttributeDTO dto);

      AttributeDTO toDto(AttributeEntity entity);

      Set<AttributeEntity> toEntitySet(List<AttributeDTO> dtoList);

      List<AttributeDTO> toDtoList(Set<AttributeEntity> entitySet);
}