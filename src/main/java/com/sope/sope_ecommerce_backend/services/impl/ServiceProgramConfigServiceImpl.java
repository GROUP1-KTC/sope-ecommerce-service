package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.response.ServiceProgramDTO;
import com.sope.sope_ecommerce_backend.entities.ServiceProgramConfig;
import com.sope.sope_ecommerce_backend.mapper.ServiceProgramConfigMapper;
import com.sope.sope_ecommerce_backend.repositories.ServiceProgramConfigRepository;
import com.sope.sope_ecommerce_backend.services.ServiceProgramConfigService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceProgramConfigServiceImpl implements ServiceProgramConfigService {

	private final ServiceProgramConfigRepository repository;
	private final ServiceProgramConfigMapper mapper;

	@Override
	@Transactional
	public ServiceProgramDTO patchConfig(ServiceProgramDTO dto) {
		ServiceProgramConfig config = repository.findAll().stream()
				.findFirst()
				.orElse(new ServiceProgramConfig());

		if (dto.dailyFeeForAds() != null) {
			config.setDailyFeeForAds(dto.dailyFeeForAds());
		}

		if (dto.platformFeePercentageForFlashSale() != null) {
			config.setPlatformFeePercentageForFlashSale(dto.platformFeePercentageForFlashSale());
		}

		return mapper.toDto(repository.save(config));
	}

	public BigDecimal getDailyFeeForAds() {
		return repository.findById(1L)
				.map(ServiceProgramConfig::getDailyFeeForAds)
				.orElseThrow(() -> new IllegalStateException("Config not found"));
	}

	public BigDecimal getPlatformFeePercentageForFlashSale() {
		return repository.findById(1L)
				.map(ServiceProgramConfig::getPlatformFeePercentageForFlashSale)
				.orElseThrow(() -> new IllegalStateException("Config not found"));
	}
}
