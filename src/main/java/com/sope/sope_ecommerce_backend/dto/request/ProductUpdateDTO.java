package com.sope.sope_ecommerce_backend.dto.request;

import java.util.List;

import com.sope.sope_ecommerce_backend.dto.response.ProductDetailDTO;

public record ProductUpdateDTO(
		Boolean hidden,
		String description,

		List<String> imageUrlsToKeep,

		List<ProductVariantRequestDTO> variants,

		List<ProductDetailDTO> productDetails

) {
}
