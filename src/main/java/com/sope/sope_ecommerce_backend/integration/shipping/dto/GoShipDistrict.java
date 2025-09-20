package com.sope.sope_ecommerce_backend.integration.shipping.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class GoShipDistrict {
    private String id;
    private String name;
    @JsonProperty("city_id")
    private String cityId;
    private List<GoShipWard> wards = new ArrayList<>();
}
