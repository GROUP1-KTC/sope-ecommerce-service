package com.sope.sope_ecommerce_backend.modules.product.dto;

import com.sope.sope_ecommerce_backend.modules.product.enums.StatusProduct;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Data
public class ProductResponse {
      private UUID productId;
      private String name;
      private BigDecimal defaultPrice;
      private String brand;
      private String description;
      private String defaultImage;
      private boolean hidden;
      private StatusProduct status;
      private String slug;
      private LocalDateTime createdAt;
      private CategoryInfo category;
      private ShopInfo shop;
      private List<ProductVariantResponse> variants; // Thêm dòng này

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