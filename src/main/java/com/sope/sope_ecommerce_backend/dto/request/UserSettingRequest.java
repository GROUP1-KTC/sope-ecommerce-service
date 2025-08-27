package com.sope.sope_ecommerce_backend.dto.request;

public record UserSettingRequest (
        boolean orderUpdateNoti,
        boolean promotionNoti,
        boolean surveyNoti
){}
