package com.sope.sope_ecommerce_backend.entities;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Dimension {
      private BigDecimal length; // Chiều dài
      private BigDecimal width; // Chiều rộng
      private BigDecimal height; // Chiều cao
}