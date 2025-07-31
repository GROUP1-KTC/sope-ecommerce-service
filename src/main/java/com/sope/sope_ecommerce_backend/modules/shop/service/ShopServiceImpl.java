package com.sope.sope_ecommerce_backend.modules.shop.service;

import com.sope.sope_ecommerce_backend.modules.shop.dto.ShopDTO;
import com.sope.sope_ecommerce_backend.modules.shop.entity.Shop;
import com.sope.sope_ecommerce_backend.modules.shop.repository.ShopRepository;
import com.sope.sope_ecommerce_backend.modules.user.entity.User;
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
    public ShopDTO createShop(ShopDTO shopDTO, User user) {
        if (shopRepository.existsByName(shopDTO.getName())) {
            throw new RuntimeException("Shop name already exists");
        }
        if (shopRepository.existsByEmail(shopDTO.getEmail())) {
            throw new RuntimeException("Shop email already exists");
        }

        Shop shop = new Shop();
        shop.setId(UUID.randomUUID());
        shop.setUser(user);
        shop.setName(shopDTO.getName());
        shop.setPhone(shopDTO.getPhone());
        shop.setEmail(shopDTO.getEmail());
        shop.setAddress(shopDTO.getAddress());
        shop.setDescription(shopDTO.getDescription());
        shop.setLogoUrl(shopDTO.getLogoUrl());
        shop.setMall(shopDTO.isMall());
        shop.setStatus(Shop.Status.valueOf(shopDTO.getStatus() != null ? shopDTO.getStatus() : "ACTIVE"));
        shop.setCreatedAt(LocalDateTime.now());
        shop.setUpdatedAt(LocalDateTime.now());

        shop = shopRepository.save(shop);
        return convertToDTO(shop);
    }

    @Transactional(readOnly = true)
    public ShopDTO getShopById(UUID id) {
        Shop shop = shopRepository.findById(id)
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
        Shop shop = shopRepository.findById(id)
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
        shop.setStatus(Shop.Status.valueOf(shopDTO.getStatus() != null ? shopDTO.getStatus() : "ACTIVE"));
        shop.setUpdatedAt(LocalDateTime.now());

        shop = shopRepository.save(shop);
        return convertToDTO(shop);
    }

    @Transactional
    public void deleteShop(UUID id) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        shop.setStatus(Shop.Status.DELETED);
        shop.setUpdatedAt(LocalDateTime.now());
        shopRepository.save(shop);
    }

    private ShopDTO convertToDTO(Shop shop) {
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