package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ProductVariantDTO {
      private UUID productVariantId;
      private BigDecimal price;
      private int stock;
      private int sold;
      private UUID productId;
      private List<AttributeDTO> attributes;
      private String imageVariant;
}