package com.sope.sope_ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.dto.response.ProductDetailDTO;

public record ProductCreateDTO(
		@NotBlank(message = "Name cannot be blank") String name,

		String brand,

		String description,

		boolean hidden,

		@NotNull(message = "Category ID cannot be null") UUID categoryId,

		@NotNull(message = "Shop ID cannot be null") UUID shopId,

		List<ProductVariantRequestDTO> variants,

		List<ProductDetailDTO> productDetails) {

}