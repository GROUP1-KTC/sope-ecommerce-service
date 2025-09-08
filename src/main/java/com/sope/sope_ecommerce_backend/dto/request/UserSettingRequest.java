package com.sope.sope_ecommerce_backend.dto.request;

public record UserSettingRequest (
        Boolean orderUpdateNoti,
        Boolean promotionNoti,
        Boolean surveyNoti
){}
