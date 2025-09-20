package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.AddressCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.AddressUpdateRequest;
import com.sope.sope_ecommerce_backend.dto.response.AddressResponse;
import com.sope.sope_ecommerce_backend.entities.Address;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.mapper.AddressMapper;
import com.sope.sope_ecommerce_backend.repositories.AddressRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.services.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponse addAddress(UUID userId, AddressCreateRequest request) {
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean shouldBeDefault = shouldSetAsDefault(userId, request.isDefault());

        if (shouldBeDefault) {
            unsetExistingDefaults(userId);
        }

        Address address = new Address();
        address.setAppUser(appUser);
        address.setRecipientName(request.recipientName());
        address.setPhoneNumber(request.phoneNumber());
        address.setStreet(request.street());
        address.setWard(request.ward());
        address.setDistrict(request.district());
        address.setCity(request.city());
        address.setCountry(request.country());
        address.setDefault(shouldBeDefault);

        Address saved = addressRepository.save(address);
        return addressMapper.toResponse(saved);
    }

    private boolean shouldSetAsDefault(UUID userId, boolean requestDefault) {
        boolean hasNoAddress = addressRepository.findByAppUser_Id(userId).isEmpty();
        return hasNoAddress || requestDefault;
    }

    private void unsetExistingDefaults(UUID userId) {
        addressRepository.findByAppUser_Id(userId)
                .forEach(a -> {
                    if (a.isDefault()) {
                        a.setDefault(false);
                        addressRepository.save(a);
                    }
                });
    }

    @Override
    public AddressResponse updateAddress(UUID userId, UUID addressId, AddressUpdateRequest request) {
        Address address = addressRepository.findById(addressId)
                .filter(a -> a.getAppUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (Boolean.TRUE.equals(request.isDefault())) {
            addressRepository.findByAppUser_Id(userId)
                    .forEach(a -> {
                        if (a.isDefault()) {
                            a.setDefault(false);
                            addressRepository.save(a);
                        }
                    });
            address.setDefault(true);
        }

        address.setRecipientName(request.recipientName());
        address.setPhoneNumber(request.phoneNumber());
        address.setStreet(request.street());
        address.setWard(request.ward());
        address.setDistrict(request.district());
        address.setCity(request.city());
        address.setCountry(request.country());

        Address saved = addressRepository.save(address);
        return addressMapper.toResponse(saved);
    }

    @Override
    public List<AddressResponse> getUserAddresses(UUID userId) {
        return addressRepository.findByAppUser_Id(userId)
                .stream()
                .map(addressMapper::toResponse)
                .toList();
    }

    @Override
    public AddressResponse setDefaultAddress(UUID userId, UUID addressId) {
        addressRepository.findByAppUser_Id(userId)
                .forEach(a -> {
                    if (a.isDefault()) {
                        a.setDefault(false);
                        addressRepository.save(a);
                    }
                });

        Address address = addressRepository.findById(addressId)
                .filter(a -> a.getAppUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setDefault(true);
        Address saved = addressRepository.save(address);
        return addressMapper.toResponse(saved);
    }

    @Override
    public AddressResponse getDefaultAddress(UUID userId) {
        return addressRepository.findByAppUser_IdAndIsDefaultTrue(userId)
                .map(addressMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Default address not found"));
    }

    @Override
    @Transactional
    public Address getOrCreateAddress(UUID userId, AddressCreateRequest request) {
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressMapper.toEntity(request);
        address.setAppUser(appUser);

        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public void deleteAddress(UUID userId, UUID addressId) {
        Address address = addressRepository.findById(addressId)
                .filter(a -> a.getAppUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Address not found"));

        addressRepository.delete(address);
    }

    @Override
    public Address getAddressEntityById(UUID addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }

}
