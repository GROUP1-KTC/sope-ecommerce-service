package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.UserSettingRequest;
import com.sope.sope_ecommerce_backend.dto.response.UserSettingResponse;
import com.sope.sope_ecommerce_backend.entities.UserSetting;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserSettingMapper {

    UserSettingResponse toResponse(UserSetting setting);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(UserSettingRequest request, @MappingTarget UserSetting setting);
}

