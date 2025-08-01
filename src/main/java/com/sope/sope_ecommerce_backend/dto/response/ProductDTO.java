package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.enums.StatusProduct;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Data
public class ProductDTO {
      private UUID productId;
      private String name;
      private BigDecimal defaultPrice;
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
      private int stock;
      private int sold;
      private ShopInfo shop;
      private List<ProductVariantDTO> variants;
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