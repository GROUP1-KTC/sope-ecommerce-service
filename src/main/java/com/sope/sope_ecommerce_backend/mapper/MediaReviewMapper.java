package com.sope.sope_ecommerce_backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sope.sope_ecommerce_backend.dto.response.ReviewMediaDTO;
import com.sope.sope_ecommerce_backend.entities.ReviewMediaEntity;

@Mapper(componentModel = "spring")
public interface MediaReviewMapper {
  @Mapping(target = "review", ignore = true)
  ReviewMediaEntity toEntity(ReviewMediaDTO dto);

  ReviewMediaDTO toDto(ReviewMediaEntity entity);
}
