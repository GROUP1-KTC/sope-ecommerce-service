package com.sope.sope_ecommerce_backend.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sope.sope_ecommerce_backend.dto.form.ShopCreateForm;
import com.sope.sope_ecommerce_backend.dto.request.ShopCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.ShopIdentificationRequest;
import com.sope.sope_ecommerce_backend.dto.request.ShopUpdateRequest;
import com.sope.sope_ecommerce_backend.dto.response.ShopResponse;
import com.sope.sope_ecommerce_backend.dto.response.ShopSearchResult;
import com.sope.sope_ecommerce_backend.entities.Shop;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.entities.ShopAddress;
import com.sope.sope_ecommerce_backend.entities.ShopIdentification;
import com.sope.sope_ecommerce_backend.mapper.ShopMapper;
import com.sope.sope_ecommerce_backend.repositories.ShopIdentificationRepository;
import com.sope.sope_ecommerce_backend.repositories.ShopRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.services.FileUploadService;
import com.sope.sope_ecommerce_backend.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    private final ShopMapper shopMapper;

    private final UserRepository userRepository;

    private final FileUploadService fileUploadService;

    private final ObjectMapper objectMapper;

    private final ShopIdentificationRepository shopIdentificationRepository;

    @Override
    public ShopResponse createShop(ShopCreateRequest request, UUID userId) {
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại: " + userId));

        Shop shop = shopMapper.toEntity(request);
        shop.setAppUser(appUser);

        if (request.address() != null) {
            ShopAddress address = ShopAddress.builder()
                    .street(request.address().street())
                    .ward(request.address().ward())
                    .district(request.address().district())
                    .city(request.address().city())
                    .country(request.address().country())
                    .shop(shop)
                    .build();
            shop.setAddress(address);
        }

        Shop savedShop = shopRepository.save(shop);

        return shopMapper.toResponse(savedShop);
    }

    @Transactional
    @Override
    public ShopResponse createShop(ShopCreateForm form, UUID userId) {
        // parse JSON sang ShopCreateRequest
        ShopCreateRequest request;
        try {
            request = objectMapper.readValue(form.getRequestJson(), ShopCreateRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid shop metadata JSON", e);
        }

        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại: " + userId));

        // Upload các file
        ShopIdentificationRequest idReq = request.identification();
        if (idReq == null || idReq.idName() == null || idReq.idNumber() == null || idReq.idType() == null) {
            throw new IllegalArgumentException("Thông tin định danh không đầy đủ");
        }

        String logoUrl = form.getLogoFile() != null ? fileUploadService.uploadImage(form.getLogoFile()) : request.logoUrl();
        String taxUrl = form.getTaxFile() != null ? fileUploadService.uploadImage(form.getTaxFile()) : request.taxDocumentUrl();
        String idFrontUrl = form.getIdFront() != null ? fileUploadService.uploadImage(form.getIdFront()) : idReq.idFront();
        String idBackUrl = form.getIdBack() != null ? fileUploadService.uploadImage(form.getIdBack()) : idReq.idBack();
        String selfieUrl = form.getSelfie() != null ? fileUploadService.uploadImage(form.getSelfie()) : idReq.selfie();

        Shop shop = shopMapper.toEntity(request);
        shop.setAppUser(appUser);
        shop.setLogoUrl(logoUrl);
        shop.setTaxDocumentUrl(taxUrl);

        if (request.address() != null) {
            ShopAddress address = ShopAddress.builder()
                    .senderName(request.address().senderName())
                    .senderPhone(request.address().senderPhone())
                    .street(request.address().street())
                    .ward(request.address().ward())
                    .district(request.address().district())
                    .city(request.address().city())
                    .country(request.address().country())
                    .shop(shop)
                    .build();
            shop.setAddress(address);
        }

        ShopIdentification identification = ShopIdentification.builder()
                .idType(idReq.idType())
                .idName(idReq.idName())
                .idNumber(idReq.idNumber())
                .idFront(idFrontUrl)
                .idBack(idBackUrl)
                .selfie(selfieUrl)
                .shop(shop)
                .build();

        shop.setIdentification(identification);

        Shop savedShop = shopRepository.save(shop);

        return shopMapper.toResponse(savedShop);
    }

    @Override
    public ShopResponse getShop(UUID userId) {
        Shop shop = shopRepository.findByAppUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Shop không tồn tại" + userId));

        return shopMapper.toResponse(shop);
    }

    @Override
    public  List<ShopResponse> getAllShops() {
        List<Shop> shops = shopRepository.findAll();
        return shops.stream().map(shopMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public ShopResponse getShopById(UUID shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found with id: " + shopId));
        return shopMapper.toResponse(shop);
    }


    @Override
    public Shop getShopEntityById(UUID shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop không tồn tại: " + shopId));
    }

    @Override
    public ShopResponse updateShop(ShopUpdateRequest request, UUID currentUserId) {
        Shop shop = shopRepository.findByAppUser_Id(currentUserId)
                .orElseThrow(() -> new RuntimeException("Shop không tồn tại: " + currentUserId));

        if (!shop.getAppUser().getId().equals(currentUserId)) {
            throw new RuntimeException("Bạn không có quyền sửa shop này");
        }

        if (request.name() != null) {
            shop.setName(request.name());
        }

        if (request.phone() != null) {
            shop.setPhone(request.phone());
        }

        if (request.email() != null) {
            shop.setEmail(request.email());
        }

        if (request.description() != null) {
            shop.setDescription(request.description());
        }

        if (request.logoUrl() != null) {
            shop.setLogoUrl(request.logoUrl());
        }

        if (request.isMall() != null) {
            shop.setMall(request.isMall());
        }

        if(request.address() != null){
            ShopAddress address = getShopAddress(request, shop);
            shop.setAddress(address);
        }

        shopMapper.updateShopFromDTO(request, shop);
        shop.setUpdatedAt(LocalDateTime.now());

        return shopMapper.toResponse(shopRepository.save(shop));
    }

    private static ShopAddress getShopAddress(ShopUpdateRequest request, Shop shop) {
        ShopAddress address = shop.getAddress();
        if (address == null) {
            address = new ShopAddress();
            address.setShop(shop);
        }
        if (request.address().street() != null) {
            address.setStreet(request.address().street());
        }
        if (request.address().ward() != null) {
            address.setWard(request.address().ward());
        }
        if (request.address().district() != null) {
            address.setDistrict(request.address().district());
        }
        if (request.address().city() != null) {
            address.setCity(request.address().city());
        }
        if (request.address().country() != null) {
            address.setCountry(request.address().country());
        }
        return address;
    }

    @Override
    public ShopResponse changeShopStatus(UUID shopId, Shop.Status shopStatus) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy shop với id: " + shopId));

        shop.setStatus(shopStatus);
        shop.setUpdatedAt(LocalDateTime.now());

        Shop savedShop = shopRepository.save(shop);

        return shopMapper.toResponse(savedShop);
    }

    @Override
    public List<ShopSearchResult> searchShopsByName(String name) {
        return shopRepository.searchShopsByName(name);
    }


}