package com.sope.sope_ecommerce_backend.entities;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_program_config")
public class ServiceProgramConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private BigDecimal dailyFeeForAds;
	private BigDecimal platformFeePercentageForFlashSale;
}
