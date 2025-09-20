package com.sope.sope_ecommerce_backend.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sope.sope_ecommerce_backend.dto.request.CategoryCreateDTO;
import com.sope.sope_ecommerce_backend.dto.response.CategoryDTO;
import com.sope.sope_ecommerce_backend.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
      @Mapping(target = "parentId", source = "parent.id")
      CategoryDTO toDto(Category category);

      List<CategoryDTO> toDtoList(List<Category> categories);

      @Mapping(target = "imageForParent", ignore = true)
      Category toEntity(CategoryCreateDTO dto);
}
