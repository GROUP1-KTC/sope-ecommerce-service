package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.ComplaintRequest;
import com.sope.sope_ecommerce_backend.dto.response.ComplaintResponse;
import com.sope.sope_ecommerce_backend.entities.ComplaintEntity;

import java.util.List;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ComplaintMapper {
  @Mapping(target = "reporterId", source = "reporter.id")
  @Mapping(target = "reporterUsername", source = "reporter.username")
  @Mapping(target = "status", source = "statusComplaint")
  ComplaintResponse toResponse(ComplaintEntity entity);

  List<ComplaintResponse> toDtoList(List<ComplaintEntity> entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "reporter", ignore = true)
  @Mapping(target = "statusComplaint", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "solvedAt", ignore = true)
  ComplaintEntity toEntity(ComplaintRequest request);
}
