package com.sope.sope_ecommerce_backend.integration.shipping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoShipCity{
    private String id;
    private String name;
    @Builder.Default
    private List<GoShipDistrict> districts = new ArrayList<>();
}
