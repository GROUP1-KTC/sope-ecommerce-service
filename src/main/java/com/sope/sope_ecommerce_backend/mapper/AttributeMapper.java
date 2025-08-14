package com.sope.sope_ecommerce_backend.mapper;

import java.util.List;
import java.util.Set;

import com.sope.sope_ecommerce_backend.entities.Attribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sope.sope_ecommerce_backend.dto.response.AttributeDTO;

@Mapper(componentModel = "spring")
public interface AttributeMapper {
      @Mapping(target = "attributeId", ignore = true)
      Attribute toEntity(AttributeDTO dto);

      AttributeDTO toDto(Attribute entity);

      Set<Attribute> toEntitySet(List<AttributeDTO> dtoList);

      List<AttributeDTO> toDtoList(Set<Attribute> entitySet);
}