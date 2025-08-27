package com.sope.sope_ecommerce_backend.dto.request;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateDTO(
		@NotNull(message = "Product ID cannot be null") UUID productId,
		@NotNull(message = "User ID cannot be null") UUID appUserId,
		Integer rating,
		String content) {
}
