package com.sope.sope_ecommerce_backend.dto.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ProductUpdateDTO {
      private BigDecimal defaultPrice;
      private Integer stock;
      private Boolean hidden;
      private String description;

      private List<String> imageUrlsToKeep;

      private List<ProductVariantRequestDTO> variants;
}
