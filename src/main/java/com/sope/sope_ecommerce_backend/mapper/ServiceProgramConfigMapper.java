package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.response.ServiceProgramDTO;
import com.sope.sope_ecommerce_backend.entities.ServiceProgramConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceProgramConfigMapper {

  ServiceProgramDTO toDto(ServiceProgramConfig entity);

  ServiceProgramConfig toEntity(ServiceProgramDTO request);
}