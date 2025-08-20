package com.sope.sope_ecommerce_backend.dto.request;

import java.util.List;

import com.sope.sope_ecommerce_backend.dto.response.ProductDetailDTO;

import lombok.Data;

@Data
public class ProductUpdateDTO {
      private Boolean hidden;
      private String description;

      private List<String> imageUrlsToKeep;

      private List<ProductVariantRequestDTO> variants;

      private List<ProductDetailDTO> productDetails;

}
