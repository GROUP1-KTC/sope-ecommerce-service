package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.response.LiveStreamResponse;
import com.sope.sope_ecommerce_backend.entities.LiveStream;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface LiveStreamMapper {

    @Mapping(source = "shop.id", target = "shopId")
    @Mapping(source = "live", target = "isLive")
    LiveStreamResponse toDto(LiveStream liveStream);
}
