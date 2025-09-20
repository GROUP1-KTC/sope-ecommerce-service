package com.sope.sope_ecommerce_backend.dto.response;

public record UserSettingResponse(
        boolean orderUpdateNoti,
        boolean promotionNoti,
        boolean surveyNoti
) {
}
