package com.sope.sope_ecommerce_backend.services.gateways;

import com.sope.sope_ecommerce_backend.client.GoShipApi;
import com.sope.sope_ecommerce_backend.constant.GoShipVariable;
import com.sope.sope_ecommerce_backend.dto.LocationIds;
import com.sope.sope_ecommerce_backend.dto.request.ShipmentCreationRequest;
import com.sope.sope_ecommerce_backend.dto.request.ShipmentRequest;
import com.sope.sope_ecommerce_backend.dto.response.*;
import com.sope.sope_ecommerce_backend.integration.shipping.GoshipProperties;
import com.sope.sope_ecommerce_backend.integration.shipping.dto.GoShipCity;
import com.sope.sope_ecommerce_backend.integration.shipping.dto.GoShipDistrict;
import com.sope.sope_ecommerce_backend.integration.shipping.dto.GoShipWard;
import com.sope.sope_ecommerce_backend.services.RedisService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("GOSHIP")
@Slf4j
@EnableScheduling
public class GoShippingGateway {

    private final GoShipApi goShipApi;
    private  final RedisService redisService;


    @PostConstruct
    public void init() throws Exception {
        if (redisService.get(GoShipVariable.GOSHIP_ADDRESSES) == null)
            fetchAndCacheLocations();
    }


//    @Scheduled(fixedRate = 3600000)
    public void fetchAndCacheLocations() throws Exception {
        List<GoShipCitiesResponse.DataCitiesResponse> cities = loadCities();

        ArrayList<GoShipCity> cityList = new ArrayList<>();

        for (GoShipCitiesResponse.DataCitiesResponse city : cities) {
            List<GoShipDistrictsResponse.DataDistrictsResponse> districts = loadDistrictsFromCity(city.id());

            GoShipCity cityEntity = GoShipCity.builder()
                    .id(city.id())
                    .name(city.name())
                    .build();

            // 3. Với từng district, gọi wards
            for (GoShipDistrictsResponse.DataDistrictsResponse district : districts) {
                GoShipWardResponse wardsResponse = loadWards(district.id());

                GoShipDistrict districtEntity = GoShipDistrict.builder()
                        .id(district.id())
                        .name(district.name())
                        .cityId(district.cityId())
                        .build();

                List<GoShipWard> wards = wardsResponse.data().stream()
                        .map(ward -> GoShipWard.builder()
                                .id(ward.id())
                                .name(ward.name())
                                .districtId(ward.districtId())
                                .build()
                        )
                        .collect(Collectors.toList());


                districtEntity.setWards(wards);
                cityEntity.getDistricts().add(districtEntity);
            }
            cityList.add(cityEntity);
        }
        // Lưu vào Redis
        redisService.set(
                GoShipVariable.GOSHIP_ADDRESSES,
                cityList
        );
    }

    private List<GoShipCitiesResponse.DataCitiesResponse> loadCities() {
        GoShipCitiesResponse cities = goShipApi.getCities();
        return cities.data();
    }

    private List<GoShipDistrictsResponse.DataDistrictsResponse> loadDistrictsFromCity(String cityId) {


       return goShipApi.getDistrictsFromCity(cityId).data();
    }

    private GoShipWardResponse loadWards(String districsCode) {
        return goShipApi.getWardsByDistrictCode(districsCode);
    }

    public RateResponse getShippingRates(ShipmentRequest request) {
        log.info("Fetching shipping rates from GoShip API for request: {}", request);
        LocationIds fromLocationIds = resolveLocationIds(
                request.shipment().addressFrom().city(),
                request.shipment().addressFrom().district(),
                request.shipment().addressFrom().ward()
        );

        LocationIds toLocationIds = resolveLocationIds(
                request.shipment().addressTo().city(),
                request.shipment().addressTo().district(),
                request.shipment().addressTo().ward()
        );

        ShipmentRequest updatedRequest = new ShipmentRequest(
                new ShipmentRequest.ShipmentDetails(
                        new ShipmentRequest.Address(
                                fromLocationIds.cityId(),
                                fromLocationIds.districtId(),
                                fromLocationIds.wardId()
                        ),
                        new ShipmentRequest.Address(
                                toLocationIds.cityId(),
                                toLocationIds.districtId(),
                                toLocationIds.wardId()
                        ),
                        request.shipment().parcel()
                )
        );

        System.out.println(updatedRequest + "-------------------");

        RateResponse rates = goShipApi.getRates(updatedRequest);
        log.info("Received {} shipping rates from GoShip API", rates.data().size());
        return rates;
    }

    public ShipmentResponse createShipment(ShipmentCreationRequest request) {

        LocationIds fromLocationIds = resolveLocationIds(
                request.shipment().addressFrom().city(),
                request.shipment().addressFrom().district(),
                request.shipment().addressFrom().ward()
        );

        LocationIds toLocationIds = resolveLocationIds(
                request.shipment().addressTo().city(),
                request.shipment().addressTo().district(),
                request.shipment().addressTo().ward()
        );

        ShipmentCreationRequest updatedRequest = new ShipmentCreationRequest(
                new ShipmentCreationRequest.ShipmentDetails(
                        request.shipment().rate(),
                        request.shipment().payer(),
                        new ShipmentCreationRequest.Address(
                                request.shipment().addressFrom().name(),
                                request.shipment().addressFrom().phone(),
                                request.shipment().addressFrom().street(),
                                fromLocationIds.cityId(),
                                fromLocationIds.districtId(),
                                fromLocationIds.wardId()
                        ),
                        new ShipmentCreationRequest.Address(
                                request.shipment().addressTo().name(),
                                request.shipment().addressTo().phone(),
                                request.shipment().addressTo().street(),
                                toLocationIds.cityId(),
                                toLocationIds.districtId(),
                                toLocationIds.wardId()
                        ),
                        request.shipment().parcel()
                )
        );

        log.info("Creating shipment with GoShip API for request: {}", updatedRequest);

        return goShipApi.createShipment(updatedRequest);
    }


    public LocationIds resolveLocationIds(String cityName, String districtName, String wardName) {
        String normalizedCityName = cityName.toLowerCase().trim();
        String normalizedDistrictName = districtName.toLowerCase().trim();
        String normalizedWardName = wardName.toLowerCase().trim();

        List<GoShipCity> cityList = (List<GoShipCity>) redisService.get(GoShipVariable.GOSHIP_ADDRESSES);
        if (cityList == null) throw new RuntimeException("Locations cache is empty");

        // 3. Tìm city
        GoShipCity city = cityList.stream()
                .filter(c -> c.getName().toLowerCase().trim().equals(normalizedCityName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("City not found: " + cityName));

        // 4. Tìm district
        GoShipDistrict district = city.getDistricts().stream()
                .filter(d -> d.getName().toLowerCase().trim().equals(normalizedDistrictName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("District not found: " + districtName));

        // 5. Tìm ward
        GoShipWard ward = district.getWards().stream()
                .filter(w -> w.getName().toLowerCase().trim().equals(normalizedWardName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ward not found: " + wardName));

        // 6. Trả về code của 3 cấp
        return LocationIds.builder()
                .cityId(city.getId())
                .districtId(district.getId())
                .wardId(ward.getId())
                .build();
    }
}

