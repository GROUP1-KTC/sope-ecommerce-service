package com.sope.sope_ecommerce_backend.dto;


import lombok.Builder;

@Builder
public record LocationIds(String cityId, String districtId, String wardId) {}