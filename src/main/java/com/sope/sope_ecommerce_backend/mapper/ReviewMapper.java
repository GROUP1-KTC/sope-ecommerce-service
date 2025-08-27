package com.sope.sope_ecommerce_backend.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sope.sope_ecommerce_backend.dto.request.ReviewCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ReviewUpdateDTO;
import com.sope.sope_ecommerce_backend.dto.response.ReviewDTO;
import com.sope.sope_ecommerce_backend.entities.ReviewEntity;

@Mapper(componentModel = "spring", uses = { MediaReviewMapper.class, MultipartFileMapper.class })
public interface ReviewMapper {
      @Mapping(source = "appUser.id", target = "user.id")
      @Mapping(source = "appUser.username", target = "user.username")
      @Mapping(source = "product.productId", target = "product.productId")
      @Mapping(source = "product.name", target = "product.name")
      ReviewDTO toDto(ReviewEntity review); // GET

      @Mapping(source = "appUser.id", target = "user.id")
      @Mapping(source = "appUser.username", target = "user.username")
      @Mapping(source = "product.productId", target = "product.productId")
      @Mapping(source = "product.name", target = "product.name")
      List<ReviewDTO> toDtoList(List<ReviewEntity> entities); // GET

      // List<ReviewDTO> toDtoList(List<ReviewEntity> reviewEntities);

      @Mapping(target = "createdAt", ignore = true)
      @Mapping(target = "updatedAt", ignore = true)
      @Mapping(target = "appUser", ignore = true)
      @Mapping(target = "product", ignore = true)
      @Mapping(target = "reviewId", ignore = true)
      @Mapping(target = "mediaList", ignore = true) // handled separately
      ReviewEntity toEntity(ReviewCreateDTO reviewCreateDTO); // POST

      @Mapping(target = "createdAt", ignore = true)
      @Mapping(target = "updatedAt", ignore = true)
      @Mapping(target = "appUser", ignore = true)
      @Mapping(target = "product", ignore = true)
      @Mapping(target = "reviewId", ignore = true)
      @Mapping(target = "mediaList", ignore = true)
      @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
      void updateReviewMapper(ReviewUpdateDTO reviewCreateDTO, @MappingTarget ReviewEntity entity);
}
