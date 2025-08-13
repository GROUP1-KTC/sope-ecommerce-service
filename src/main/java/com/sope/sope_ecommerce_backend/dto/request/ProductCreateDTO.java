package com.sope.sope_ecommerce_backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class ProductCreateDTO {
      @NotBlank(message = "Product name cannot be blank")
      private String name;

      @NotNull(message = "Default price cannot be null")
      @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
      private BigDecimal defaultPrice;

      private String brand;

      @Min(value = 0, message = "Stock cannot be negative")
      private int stock;

      private String description;

      private boolean hidden;

      @NotNull(message = "Category ID cannot be null")
      private UUID categoryId;

      @NotNull(message = "Shop ID cannot be null")
      private UUID shopId;

      private List<ProductVariantRequestDTO> variants;

}