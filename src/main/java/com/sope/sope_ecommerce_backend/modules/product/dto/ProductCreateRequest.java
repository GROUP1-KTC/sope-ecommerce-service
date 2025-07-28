package com.sope.sope_ecommerce_backend.modules.product.dto;

import com.sope.sope_ecommerce_backend.modules.product.enums.StatusProduct;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductCreateRequest {
      @NotBlank(message = "Product name cannot be blank")
      private String name;

      @NotNull(message = "Default price cannot be null")
      @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
      private BigDecimal defaultPrice;

      private String brand;

      private String description;

      @NotBlank(message = "Default image cannot be blank")
      private String defaultImage;

      private boolean hidden = false;

      private StatusProduct status = StatusProduct.PENDING;

      @NotNull(message = "Category ID cannot be null")
      private UUID categoryId;

      @NotNull(message = "Shop ID cannot be null")
      private UUID shopId;
}