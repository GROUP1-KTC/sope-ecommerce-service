package com.sope.sope_ecommerce_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.enums.ProgramStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flash_sale_programs")
public class FlashSaleProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "product_variant_id", nullable = false)
	private ProductVariant productVariant;

	private BigDecimal discountPercentage;
	private BigDecimal platformFeePercentage;

	@Enumerated(EnumType.STRING)
	private ProgramStatus status;

	private LocalDate saleDate;
	private LocalTime startTime;
	private LocalTime endTime;

	private boolean active;
}
