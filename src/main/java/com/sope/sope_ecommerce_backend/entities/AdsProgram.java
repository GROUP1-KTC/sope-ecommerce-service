package com.sope.sope_ecommerce_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.enums.ProgramStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ads_programs")
public class AdsProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Enumerated(EnumType.STRING)
	private ProgramStatus status;

	private BigDecimal dailyFee;

	private LocalDate startDate;
	private LocalDate endDate;

	private boolean active;
}
