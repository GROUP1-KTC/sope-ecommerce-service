package com.sope.sope_ecommerce_backend.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductUpdateDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductDTO;
import com.sope.sope_ecommerce_backend.entities.Product;

@Mapper(componentModel = "spring", uses = { ProductVariantMapper.class, ImageMapper.class, MultipartFileMapper.class })
public interface ProductMapper {

      ProductDTO toDto(Product product); // GET

      List<ProductDTO> toDtoList(List<Product> productEntities);

      // POST PUT
      @Mapping(target = "productId", ignore = true)
      @Mapping(target = "sold", ignore = true)
      @Mapping(target = "slug", ignore = true)
      @Mapping(target = "createdAt", ignore = true)
      @Mapping(target = "updatedAt", ignore = true)
      @Mapping(target = "reviews", ignore = true)
      @Mapping(target = "category", ignore = true)
      @Mapping(target = "shop", ignore = true)
      @Mapping(target = "wishlists", ignore = true)
      @Mapping(target = "productDetails", ignore = true)
      @Mapping(target = "imagesList", ignore = true) // File xử lý trong service
      @Mapping(target = "defaultImage", ignore = true) // File xử lý trong service
      @Mapping(target = "defaultVideoIntro", ignore = true) // File xử lý trong service
      @Mapping(target = "status", ignore = true)
      @Mapping(target = "variants", source = "variants")
      Product toEntity(ProductCreateDTO productCreateDTO); // POST

      @Mapping(target = "shop", ignore = true)
      @Mapping(target = "createdAt", ignore = true)
      @Mapping(target = "status", ignore = true)
      @Mapping(target = "wishlists", ignore = true)
      @Mapping(target = "productDetails", ignore = true)
      @Mapping(target = "imagesList", ignore = true)
      @Mapping(target = "defaultImage", ignore = true)
      @Mapping(target = "defaultVideoIntro", ignore = true)
      @Mapping(target = "sold", ignore = true)
      @Mapping(target = "slug", ignore = true)
      @Mapping(target = "productId", ignore = true)
      @Mapping(target = "updatedAt", ignore = true)
      @Mapping(target = "category", ignore = true)
      @Mapping(target = "reviews", ignore = true)
      @Mapping(target = "name", ignore = true)
      @Mapping(target = "brand", ignore = true)
      @Mapping(target = "variants", ignore = true)
      @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
      void updateEntityFromDto(ProductUpdateDTO productUpdateDTO, @MappingTarget Product entity);

}
