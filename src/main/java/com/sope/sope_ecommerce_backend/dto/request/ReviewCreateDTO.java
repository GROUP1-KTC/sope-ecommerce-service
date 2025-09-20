package com.sope.sope_ecommerce_backend.dto.request;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateDTO(
		@NotNull(message = "Product Variant ID cannot be null") UUID productVariantId,
		@NotNull(message = "User ID cannot be null") UUID appUserId,
		Integer rating,
		String content) {
}
