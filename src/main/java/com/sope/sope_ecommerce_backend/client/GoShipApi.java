package com.sope.sope_ecommerce_backend.client;


import com.sope.sope_ecommerce_backend.configuration.GoShipFeignConfig;
import com.sope.sope_ecommerce_backend.dto.request.ShipmentCreationRequest;
import com.sope.sope_ecommerce_backend.dto.request.ShipmentRequest;
import com.sope.sope_ecommerce_backend.dto.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "goship",
        url = "${shipping-unit.goship.base-url}",
        configuration = GoShipFeignConfig.class
)

public interface GoShipApi {
    @PostMapping("/shipments")
    ShipmentResponse createShipment(ShipmentCreationRequest request);

    @PostMapping("/rates")
    RateResponse getRates(ShipmentRequest request);

    @GetMapping("/cities")
    GoShipCitiesResponse getCities();

    @GetMapping("/cities/{code}/districts")
    GoShipDistrictsResponse getDistrictsFromCity(@PathVariable("code") String code);


    @GetMapping("/districts")
    GoShipDistrictsResponse getDistricts(@RequestParam("size") int size);

    @GetMapping("/districts/{code}/wards")
    GoShipWardResponse getWardsByDistrictCode(@PathVariable("code") String code);

}
