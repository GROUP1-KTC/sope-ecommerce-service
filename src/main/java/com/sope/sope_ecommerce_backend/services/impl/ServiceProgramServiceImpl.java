package com.sope.sope_ecommerce_backend.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.sope.sope_ecommerce_backend.dto.request.AdsProgramRequest;
import com.sope.sope_ecommerce_backend.dto.request.FlashSaleProgramRequest;
import com.sope.sope_ecommerce_backend.dto.response.AdsProgramDTO;
import com.sope.sope_ecommerce_backend.dto.response.FlashSaleProgramDTO;
import com.sope.sope_ecommerce_backend.entities.AdsProgram;
import com.sope.sope_ecommerce_backend.entities.FlashSaleProgram;
import com.sope.sope_ecommerce_backend.entities.Product;
import com.sope.sope_ecommerce_backend.entities.ProductVariant;
import com.sope.sope_ecommerce_backend.enums.ProgramStatus;
import com.sope.sope_ecommerce_backend.mapper.AdsProgramMapper;
import com.sope.sope_ecommerce_backend.mapper.FlashSaleProgramMapper;
import com.sope.sope_ecommerce_backend.repositories.AdsProgramRepository;
import com.sope.sope_ecommerce_backend.repositories.FlashSaleProgramRepository;
import com.sope.sope_ecommerce_backend.repositories.ProductRepository;
import com.sope.sope_ecommerce_backend.repositories.ProductVariantRepository;
import com.sope.sope_ecommerce_backend.services.ServiceProgramService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceProgramServiceImpl implements ServiceProgramService {

	private final AdsProgramRepository adsRepository;
	private final FlashSaleProgramRepository flashSaleRepository;
	private final ProductRepository productRepository;
	private final ProductVariantRepository variantRepository;
	private final AdsProgramMapper adsMapper;
	private final FlashSaleProgramMapper flashSaleMapper;
	private final ServiceProgramConfigServiceImpl serviceProgramConfigServiceImpl;

	// ========================= ADS =========================
	@Override
	@Transactional
	public AdsProgramDTO createAdsProgram(AdsProgramRequest request) {
		Product product = productRepository.findById(request.productId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found"));

		if (request.durationDays() < 1 || request.durationDays() > 7) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ads duration must be 1-7 days");
		}

		BigDecimal dailyFee = serviceProgramConfigServiceImpl.getDailyFeeForAds();
		if (dailyFee == null) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Daily fee not configured");
		}

		AdsProgram entity = AdsProgram.builder()
				.product(product)
				.startDate(request.startDate())
				.endDate(request.startDate().plusDays(request.durationDays() - 1))
				.dailyFee(dailyFee)
				.status(ProgramStatus.PENDING)
				.active(false)
				.build();

		adsRepository.save(entity);
		return adsMapper.toDto(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AdsProgramDTO> getActiveAdsPrograms() {
		LocalDate today = LocalDate.now();
		return adsRepository.findAll().stream()
				.filter(a -> !a.getStartDate().isAfter(today) && !a.getEndDate().isBefore(today))
				.map(adsMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public AdsProgramDTO changeAdsStatus(UUID id, ProgramStatus request) {
		AdsProgram entity = adsRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ads program not found"));

		ProgramStatus current = entity.getStatus();
		ProgramStatus newStatus = request;

		// Validate action
		if (newStatus == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
		}

		// Quy tắc: seller chỉ được hủy khi còn PENDING
		if (newStatus == ProgramStatus.CANCELLED && current != ProgramStatus.PENDING) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cancel after admin approval");
		}

		// Quy tắc: admin chỉ có thể APPROVED / REJECTED / COMPLETED khi chưa CANCELLED
		if (List.of(ProgramStatus.APPROVED, ProgramStatus.REJECTED, ProgramStatus.COMPLETED)
				.contains(newStatus) && current == ProgramStatus.CANCELLED) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot approve/reject a cancelled program");
		}

		// Set status
		entity.setStatus(newStatus);

		adsRepository.save(entity);
		return adsMapper.toDto(entity);
	}

	// ========================= FLASH SALE =========================

	@Override
	@Transactional
	public FlashSaleProgramDTO createFlashSale(FlashSaleProgramRequest request) {
		ProductVariant variant = variantRepository.findById(request.productVariantId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product variant not found"));

		if (request.startTime().isAfter(request.endTime()) || request.startTime().equals(request.endTime())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time must be before end time");
		}

		long durationHours = ChronoUnit.HOURS.between(request.startTime(), request.endTime());
		if (durationHours > 24) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FlashSale duration cannot exceed 24 hours");
		}

		BigDecimal platformFeePercentage = serviceProgramConfigServiceImpl.getPlatformFeePercentageForFlashSale();
		if (platformFeePercentage == null) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Platform fee percentage not configured");
		}

		FlashSaleProgram entity = FlashSaleProgram.builder()
				.productVariant(variant)
				.saleDate(request.saleDate())
				.startTime(request.startTime())
				.endTime(request.endTime())
				.discountPercentage(request.discountPercentage())
				.platformFeePercentage(platformFeePercentage)
				.status(ProgramStatus.PENDING)
				.active(false)
				.build();

		flashSaleRepository.save(entity);
		return flashSaleMapper.toDto(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<FlashSaleProgramDTO> getActiveFlashSales() {
		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now();

		return flashSaleRepository.findAll().stream()
				.filter(f -> f.getSaleDate().equals(today) &&
						!f.getStartTime().isAfter(now) &&
						!f.getEndTime().isBefore(now))
				.map(flashSaleMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public FlashSaleProgramDTO changeFlashStatus(UUID id, ProgramStatus request) {
		FlashSaleProgram entity = flashSaleRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FlashSale program not found"));

		ProgramStatus current = entity.getStatus();
		ProgramStatus newStatus = request;

		// Validate action
		if (newStatus == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
		}

		if (newStatus == ProgramStatus.CANCELLED && current != ProgramStatus.PENDING) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cancel after admin approval");
		}

		if (List.of(ProgramStatus.APPROVED, ProgramStatus.REJECTED, ProgramStatus.COMPLETED)
				.contains(newStatus) && current == ProgramStatus.CANCELLED) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot approve/reject a cancelled program");
		}

		// Set status
		entity.setStatus(newStatus);

		flashSaleRepository.save(entity);
		return flashSaleMapper.toDto(entity);
	}

}
