package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.AddressCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.AddressUpdateRequest;
import com.sope.sope_ecommerce_backend.dto.response.AddressResponse;
import com.sope.sope_ecommerce_backend.entities.Address;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.repositories.AddressRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    @Override
    public Address getAddressEntityById(UUID addressId){
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }

    @Override
    @Transactional
    public AddressResponse addAddress(UUID userId, AddressCreateRequest request) {
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.isDefault()) {
            addressRepository.findByAppUser_Id(userId)
                    .forEach(a -> {
                        if (a.isDefault()) {
                            a.setDefault(false);
                            addressRepository.save(a);
                        }
                    });
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
        address.setDefault(request.isDefault());

        Address saved = addressRepository.save(address);
        return toResponse(saved);
    }


    @Override
    @Transactional
    public Address getOrCreateAddress(UUID userId, AddressCreateRequest request){
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = new Address();
        address.setAppUser(appUser);
        address.setRecipientName(request.recipientName());
        address.setPhoneNumber(request.phoneNumber());
        address.setStreet(request.street());
        address.setWard(request.ward());
        address.setDistrict(request.district());
        address.setCity(request.city());
        address.setCountry(request.country());
        address.setDefault(request.isDefault());

        return addressRepository.save(address);
    }


    @Override
    @Transactional
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
        return toResponse(saved);
    }

    @Override
    public void deleteAddress(UUID userId, UUID addressId) {
        Address address = addressRepository.findById(addressId)
                .filter(a -> a.getAppUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Address not found"));
        addressRepository.delete(address);
    }

    @Override
    public List<AddressResponse> getUserAddresses(UUID userId) {
        return addressRepository.findByAppUser_Id(userId)
                .stream().map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
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
        return toResponse(saved);
    }

    @Override
    public AddressResponse getDefaultAddress(UUID userId) {
        return addressRepository.findByAppUser_IdAndIsDefaultTrue(userId)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Default address not found"));
    }

    private AddressResponse toResponse(Address a) {
        return new AddressResponse(
                a.getId(),
                a.getRecipientName(),
                a.getPhoneNumber(),
                a.getStreet(),
                a.getWard(),
                a.getDistrict(),
                a.getCity(),
                a.getCountry(),
                a.isDefault()
        );
    }
}
