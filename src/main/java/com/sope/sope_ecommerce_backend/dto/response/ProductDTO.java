package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.enums.StatusProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductDTO {
      private UUID productId;
      private String name;
      private String brand;
      private String description;
      private String defaultImage;
      private String defaultVideoIntro;
      private boolean hidden;
      private StatusProduct status;
      private String slug;
      private LocalDateTime createdAt;
      private LocalDateTime updatedAt;
      private CategoryInfo category;
      private ShopInfo shop;
      private List<ProductVariantDTO> variants;
      private List<ProductDetailDTO> productDetails;
      private List<ImageDTO> imagesList;

      @Data
      public static class CategoryInfo {
            private UUID id;
            private String name;
      }

      @Data
      public static class ShopInfo {
            private UUID id;
            private String name;
      }
}