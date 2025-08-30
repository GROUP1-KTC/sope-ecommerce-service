package com.sope.sope_ecommerce_backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.sope.sope_ecommerce_backend.enums.StatusProduct;

public record ProductDTO(
    UUID productId,
    String name,
    String brand,
    String description,
    String defaultImage,
    String defaultVideoIntro,
    boolean hidden,
    StatusProduct status,
    String slug,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    CategoryParentInfo category,
    ShopInfo shop,
    List<ProductVariantDTO> variants,
    List<ProductDetailDTO> productDetails,
    List<ImageDTO> imagesList) {
}
