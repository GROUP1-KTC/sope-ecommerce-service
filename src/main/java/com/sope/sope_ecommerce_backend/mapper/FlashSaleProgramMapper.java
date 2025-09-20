// FlashSaleProgramMapper.java
package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.FlashSaleProgramRequest;
import com.sope.sope_ecommerce_backend.dto.response.FlashSaleProgramDTO;
import com.sope.sope_ecommerce_backend.dto.response.VariantInfo;
import com.sope.sope_ecommerce_backend.entities.FlashSaleProgram;
import com.sope.sope_ecommerce_backend.entities.ProductVariant;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FlashSaleProgramMapper {

	@Mapping(target = "variantInfo", source = "productVariant")
	FlashSaleProgramDTO toDto(FlashSaleProgram entity);

	@Mapping(target = "productVariant", ignore = true)
	@Mapping(target = "active", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", ignore = true)
	FlashSaleProgram toEntity(FlashSaleProgramRequest request);

	@Mapping(target = "productVariantId", source = "productVariantId")
	@Mapping(target = "productName", source = "product.name")
	@Mapping(target = "price", source = "price")
	@Mapping(target = "stock", source = "stock")
	@Mapping(target = "sold", source = "sold")
	@Mapping(target = "attributes", source = "attributes")
	@Mapping(target = "imageUrl", expression = "java(variant.getImageVariant() != null && !variant.getImageVariant().isBlank() "
			+ "? variant.getImageVariant() "
			+ ": (variant.getProduct() != null ? variant.getProduct().getDefaultImage() : null))")
	VariantInfo toVariantInfo(ProductVariant variant);

}
