package com.sope.sope_ecommerce_backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sope.sope_ecommerce_backend.dto.request.ProductVariantRequestDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductVariantDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductVariantDetailDTO;
import com.sope.sope_ecommerce_backend.entities.ProductVariant;

@Mapper(componentModel = "spring", uses = { MultipartFileMapper.class, AttributeMapper.class })
public interface ProductVariantMapper {
      @Mapping(target = "productId", source = "product.productId")
      ProductVariantDTO toDto(ProductVariant entity); // GET

      @Mapping(target = "product.productId", source = "product.productId")
      @Mapping(target = "product.name", source = "product.name")
      ProductVariantDetailDTO toProductVariantDetailDTO(ProductVariant entity); // GET

      @Mapping(target = "productVariantId", ignore = true)
      @Mapping(target = "sold", ignore = true)
      @Mapping(target = "product", ignore = true)
      ProductVariant toEntity(ProductVariantRequestDTO dto); // POST

}
