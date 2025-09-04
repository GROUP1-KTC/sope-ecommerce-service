package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.AddressCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.AddressResponse;
import com.sope.sope_ecommerce_backend.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Mapping(target = "isDefault", expression = "java(address.isDefault())")
    @Mapping(target = "fullAddress", expression = "java(formatAddress(address))")
    AddressResponse toResponse(Address address);

    default String formatAddress(Address address) {
        return String.join(", ",
                address.getStreet(),
                address.getWard(),
                address.getDistrict(),
                address.getCity(),
                address.getCountry()
        );
    }

    Address toEntity(AddressCreateRequest request);
}
