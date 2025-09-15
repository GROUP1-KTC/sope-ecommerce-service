package com.sope.sope_ecommerce_backend.seeder.address;


import com.sope.sope_ecommerce_backend.entities.Address;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.repositories.AddressRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class AddressSeeder {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public void run() throws Exception {

        addressRepository.deleteAll();
        // Seed addresses for existing users
        createAddressesForUser("user1", Arrays.asList(
                new AddressData("Nguyễn Văn A", "0123456789", "123 Đường Láng", "Láng Thượng", "Đống Đa", "Hà Nội", "Vietnam", true),
                new AddressData("Nguyễn Văn A", "0123456789", "456 Nguyễn Trãi", "Thanh Xuân Trung", "Thanh Xuân", "Hà Nội", "Vietnam", false)
        ));
        createAddressesForUser("user2", Arrays.asList(
                new AddressData("Trần Thị B", "0987654321", "789 Lê Lợi", "Phường 1", "Quận 1", "TP Hồ Chí Minh", "Vietnam", true)
        ));
        createAddressesForUser("user3", Arrays.asList(
                new AddressData("Lê Văn C", "0912345678", "101 Hai Bà Trưng", "Phường 6", "Quận 3", "TP Hồ Chí Minh", "Vietnam", true)
        ));
        createAddressesForUser("user4", Arrays.asList(
                new AddressData("Phạm Thị D", "0932145678", "202 Trần Phú", "Phường 5", "TP Vũng Tàu", "Bà Rịa - Vũng Tàu", "Vietnam", true)
        ));
        createAddressesForUser("admin", Arrays.asList(
                new AddressData("Admin User", "0999999999", "303 Kim Mã", "Ngọc Khánh", "Ba Đình", "Hà Nội", "Vietnam", true)
        ));
    }

    private void createAddressesForUser(String username, List<AddressData> addressDataList) {
        // Find the user by username
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));

        // Check if user already has addresses
        if (addressRepository.findByAppUser(user).isEmpty()) {
            addressDataList.forEach(data -> {
                Address address = Address.builder()
                        .appUser(user)
                        .recipientName(data.recipientName)
                        .phoneNumber(data.phoneNumber)
                        .street(data.street)
                        .ward(data.ward)
                        .district(data.district)
                        .city(data.city)
                        .country(data.country)
                        .isDefault(data.isDefault)
                        .build();

                addressRepository.save(address);
                System.out.println("✅ Đã tạo địa chỉ cho người dùng: " + username + " tại " + data.street + ", " + data.city);
            });
        }
    }

    // Helper class to hold address data
    private record AddressData(
            String recipientName,
            String phoneNumber,
            String street,
            String ward,
            String district,
            String city,
            String country,
            boolean isDefault
    ) {}
}