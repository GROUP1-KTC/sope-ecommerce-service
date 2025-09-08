package com.sope.sope_ecommerce_backend.seeder.user;

import com.sope.sope_ecommerce_backend.entities.Shop;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.repositories.ShopRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ShopSeeder {

      private final ShopRepository shopRepository;
      private final UserRepository userRepository;

      public void run() throws Exception {
            // Lấy user vừa tạo trong UserSeeder theo username
            createShopIfNotExists(
                        "user", // username cố định của user chủ shop
                        "Shop Nguyen Van A",
                        "0909123456",
                        "shopa@example.com",
                        "123 Đường Lê Lợi, Quận 1, TP.HCM",
                        "Chuyên bán quần áo thời trang cao cấp",
                        "https://cdn.example.com/logo/shop-a.png",
                        false);
      }

      private void createShopIfNotExists(String username, String name, String phone, String email,
                  String address, String description, String logoUrl, boolean isMall) {
            // Tìm user theo username
            Optional<AppUser> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                  throw new RuntimeException("User not found with username: " + username);
            }

            AppUser user = userOpt.get();

            // Kiểm tra shop của user đã tồn tại chưa
            if (shopRepository.findByAppUser_Id(user.getId()).isEmpty()) {
                  Shop shop = new Shop();
                  shop.setAppUser(user);
                  shop.setName(name);
                  shop.setPhone(phone);
                  shop.setEmail(email);
                  shop.setDescription(description);
                  shop.setLogoUrl(logoUrl);
                  shop.setMall(isMall);
                  shop.setStatus(Shop.Status.ACTIVE);
                  shop.setCreatedAt(LocalDateTime.now());
                  shop.setUpdatedAt(LocalDateTime.now());

                  shopRepository.save(shop);
                  System.out.println("✅ Seeded shop for username: " + username + " with user_id: " + user.getId());
            }
      }
}
