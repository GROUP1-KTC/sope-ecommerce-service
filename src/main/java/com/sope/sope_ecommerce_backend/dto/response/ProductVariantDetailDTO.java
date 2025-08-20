package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.entities.Dimension;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductVariantDetailDTO {
      private UUID productVariantId;
      private BigDecimal price;
      private int stock;
      private int sold;
      private String imageVariant;
      private List<AttributeDTO> attributes;
      private ProductInfo product;
      private Dimension dimension;
      private BigDecimal weight;

      @Data
      public static class ProductInfo {
            private UUID productId;
            private String name;
      }
}