package com.sope.sope_ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.dto.response.ProductDetailDTO;

@Data
public class ProductCreateDTO {
      @NotBlank(message = "Product name cannot be blank")
      private String name;

      private String brand;

      private String description;

      private boolean hidden;

      @NotNull(message = "Category ID cannot be null")
      private UUID categoryId;

      @NotNull(message = "Shop ID cannot be null")
      private UUID shopId;

      private List<ProductVariantRequestDTO> variants;

      private List<ProductDetailDTO> productDetails;

}