package com.sope.sope_ecommerce_backend.services.gateways;

import com.sope.sope_ecommerce_backend.client.GoShipApi;
import com.sope.sope_ecommerce_backend.constant.GoShipVariable;
import com.sope.sope_ecommerce_backend.dto.LocationIds;
import com.sope.sope_ecommerce_backend.dto.request.ShipmentCreationRequest;
import com.sope.sope_ecommerce_backend.dto.request.ShipmentRequest;
import com.sope.sope_ecommerce_backend.dto.response.*;
import com.sope.sope_ecommerce_backend.integration.shipping.dto.GoShipCity;
import com.sope.sope_ecommerce_backend.integration.shipping.dto.GoShipDistrict;
import com.sope.sope_ecommerce_backend.integration.shipping.dto.GoShipWard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("GOSHIP")
@Slf4j
@EnableScheduling
public class GoShippingGateway {

    private final GoShipApi goShipApi;
    private final ApplicationContext context;

    @EventListener(ApplicationReadyEvent.class)
    @Cacheable(value = GoShipVariable.GOSHIP_NAMESPACE, key = "'" + GoShipVariable.GOSHIP_ADDRESSES + "'", unless = "#result == null")
    public List<GoShipCity> getLocations() throws Exception {
        log.info("Cache miss -> Fetching GoShip locations...");
        return fetchLocations();
    }


    @CachePut(value = GoShipVariable.GOSHIP_NAMESPACE, key = "'" + GoShipVariable.GOSHIP_ADDRESSES + "'")
    public List<GoShipCity> syncLocations() throws Exception {
        log.info("Forcing sync of GoShip locations...");
        return fetchLocations();
    }

    @CacheEvict(value = GoShipVariable.GOSHIP_NAMESPACE, key = "'" + GoShipVariable.GOSHIP_ADDRESSES + "'")
    public void clearLocationsCache() {
        log.info("Cleared GoShip locations cache.");
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledSync() throws Exception {
        log.info("Scheduled sync of GoShip locations started...");
        GoShippingGateway proxy = context.getBean(GoShippingGateway.class);
        proxy.syncLocations();
    }

    /* ================= API CALL ================= */

    private List<GoShipCity> fetchLocations() throws Exception {
        List<GoShipCitiesResponse.DataCitiesResponse> cities = Optional.ofNullable(goShipApi.getCities().data())
                .orElse(Collections.emptyList());

        if (cities.isEmpty()) {
            throw new RuntimeException("No cities returned from GoShip API");
        }

        return cities.stream()
                .map(this::buildCityWithDistricts)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private GoShipCity buildCityWithDistricts(GoShipCitiesResponse.DataCitiesResponse city) {
        List<GoShipDistrictsResponse.DataDistrictsResponse> districts = Optional.ofNullable(goShipApi.getDistrictsFromCity(city.id()).data())
                .orElse(Collections.emptyList());

        if (districts.isEmpty()) {
            log.warn("No districts found for city: {}", city.name());
            return null;
        }

        GoShipCity cityEntity = GoShipCity.builder()
                .id(city.id())
                .name(city.name())
                .build();

        districts.stream()
                .map(this::buildDistrictWithWards)
                .filter(Objects::nonNull)
                .forEach(cityEntity.getDistricts()::add);

        return cityEntity;
    }

    private GoShipDistrict buildDistrictWithWards(GoShipDistrictsResponse.DataDistrictsResponse district) {
        List<GoShipWardResponse.DataWardsResponse> wards = Optional.ofNullable(goShipApi.getWardsByDistrictCode(district.id()).data())
                .orElse(Collections.emptyList());

        if (wards.isEmpty()) {
            log.warn("No wards found for district: {}", district.name());
            return null;
        }

        GoShipDistrict districtEntity = GoShipDistrict.builder()
                .id(district.id())
                .name(district.name())
                .cityId(district.cityId())
                .wards(
                        wards.stream()
                                .map(w -> GoShipWard.builder()
                                        .id(w.id())
                                        .name(w.name())
                                        .districtId(w.districtId())
                                        .build())
                                .toList()
                )
                .build();

        return districtEntity;
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
        List<GoShipCity> cities = getLocationsUnchecked();

        String cityKey = normalize(cityName);
        String districtKey = normalize(districtName);
        String wardKey = normalize(wardName);


        GoShipCity city = cities.stream()
                .filter(c -> normalize(c.getName()).equals(cityKey))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("City not found: " + cityName));

        GoShipDistrict district = city.getDistricts().stream()
                .filter(d -> normalize(d.getName()).equals(districtKey))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("District not found: " + districtName));

        GoShipWard ward = district.getWards().stream()
                .filter(w -> normalize(w.getName()).equals(wardKey))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ward not found: " + wardName));

        log.info("Resolved location IDs - City: {} (ID: {}), District: {} (ID: {}), Ward: {} (ID: {})",
                city.getName(), city.getId(),
                district.getName(), district.getId(),
                ward.getName(), ward.getId());

        return new LocationIds(city.getId(), district.getId(), ward.getId());
    }

    private String normalize(String input) {
        return Optional.ofNullable(input).orElse("")
                .toLowerCase().trim();
    }


    public List<GoShipCity> getLocationsUnchecked() {
        try {
            GoShippingGateway proxy = context.getBean(GoShippingGateway.class);
            return proxy.getLocations();
        } catch (Exception e) {
            throw new RuntimeException("Cannot load GoShip locations", e);
        }
    }
}

