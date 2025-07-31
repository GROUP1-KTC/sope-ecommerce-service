package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.response.ShopDTO;
import com.sope.sope_ecommerce_backend.entities.ShopEntity;
import com.sope.sope_ecommerce_backend.repositories.ShopRepository;
import com.sope.sope_ecommerce_backend.services.ShopService;
import com.sope.sope_ecommerce_backend.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    @Transactional
    public ShopDTO createShop(ShopDTO shopDTO, UserEntity user) {
        if (shopRepository.existsByName(shopDTO.getName())) {
            throw new RuntimeException("Shop name already exists");
        }
        if (shopRepository.existsByEmail(shopDTO.getEmail())) {
            throw new RuntimeException("Shop email already exists");
        }

        ShopEntity shop = new ShopEntity();
        shop.setId(UUID.randomUUID());
        shop.setUser(user);
        shop.setName(shopDTO.getName());
        shop.setPhone(shopDTO.getPhone());
        shop.setEmail(shopDTO.getEmail());
        shop.setAddress(shopDTO.getAddress());
        shop.setDescription(shopDTO.getDescription());
        shop.setLogoUrl(shopDTO.getLogoUrl());
        shop.setMall(shopDTO.isMall());
        shop.setStatus(ShopEntity.Status.valueOf(shopDTO.getStatus() != null ? shopDTO.getStatus() : "ACTIVE"));
        shop.setCreatedAt(LocalDateTime.now());
        shop.setUpdatedAt(LocalDateTime.now());

        shop = shopRepository.save(shop);
        return convertToDTO(shop);
    }

    @Transactional(readOnly = true)
    public ShopDTO getShopById(UUID id) {
        ShopEntity shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        return convertToDTO(shop);
    }

    @Transactional(readOnly = true)
    public List<ShopDTO> getAllShops() {
        return shopRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ShopDTO updateShop(UUID id, ShopDTO shopDTO) {
        ShopEntity shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        if (!shop.getName().equals(shopDTO.getName()) && shopRepository.existsByName(shopDTO.getName())) {
            throw new RuntimeException("Shop name already exists");
        }
        if (!shop.getEmail().equals(shopDTO.getEmail()) && shopRepository.existsByEmail(shopDTO.getEmail())) {
            throw new RuntimeException("Shop email already exists");
        }

        shop.setName(shopDTO.getName());
        shop.setPhone(shopDTO.getPhone());
        shop.setEmail(shopDTO.getEmail());
        shop.setAddress(shopDTO.getAddress());
        shop.setDescription(shopDTO.getDescription());
        shop.setLogoUrl(shopDTO.getLogoUrl());
        shop.setMall(shopDTO.isMall());
        shop.setStatus(ShopEntity.Status.valueOf(shopDTO.getStatus() != null ? shopDTO.getStatus() : "ACTIVE"));
        shop.setUpdatedAt(LocalDateTime.now());

        shop = shopRepository.save(shop);
        return convertToDTO(shop);
    }

    @Transactional
    public void deleteShop(UUID id) {
        ShopEntity shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        shop.setStatus(ShopEntity.Status.DELETED);
        shop.setUpdatedAt(LocalDateTime.now());
        shopRepository.save(shop);
    }

    private ShopDTO convertToDTO(ShopEntity shop) {
        ShopDTO dto = new ShopDTO();
        dto.setId(shop.getId());
        dto.setName(shop.getName());
        dto.setPhone(shop.getPhone());
        dto.setEmail(shop.getEmail());
        dto.setAddress(shop.getAddress());
        dto.setDescription(shop.getDescription());
        dto.setLogoUrl(shop.getLogoUrl());
        dto.setMall(shop.isMall());
        dto.setStatus(shop.getStatus().name());
        return dto;
    }
}