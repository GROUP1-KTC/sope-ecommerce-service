package com.sope.sope_ecommerce_backend.integration.shipping.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoShipWard

{
    private String id;
    private String name;
    @JsonProperty("district_id")
    private String districtId;
}
