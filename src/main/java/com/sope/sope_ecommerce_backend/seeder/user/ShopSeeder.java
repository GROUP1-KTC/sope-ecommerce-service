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
            // Tạo bốn cửa hàng với các người dùng khác nhau
            createShopIfNotExists(
                    "user1",
                    "Shop Nguyễn Văn A",
                    "0909123456",
                    "shopa@example.com",
                    "123 Đường Lê Lợi, Quận 1, TP.HCM",
                    "Chuyên bán quần áo thời trang cao cấp",
                    "https://cdn.example.com/logo/shop-a.png",
                    false
            );

            createShopIfNotExists(
                    "user2",
                    "Shop Trần Thị B",
                    "0909876543",
                    "shopb@example.com",
                    "456 Đường Nguyễn Huệ, Quận 1, TP.HCM",
                    "Chuyên bán phụ kiện điện thoại",
                    "https://cdn.example.com/logo/shop-b.png",
                    true
            );

            createShopIfNotExists(
                    "user3",
                    "Shop Lê Văn C",
                    "0909765432",
                    "shopc@example.com",
                    "789 Đường Hai Bà Trưng, Quận 3, TP.HCM",
                    "Chuyên bán thiết bị gia dụng",
                    "https://cdn.example.com/logo/shop-c.png",
                    false
            );

            createShopIfNotExists(
                    "user4",
                    "Shop Phạm Thị D",
                    "0909654321",
                    "shopd@example.com",
                    "101 Đường Võ Văn Tần, Quận 3, TP.HCM",
                    "Chuyên bán thời trang trẻ em",
                    "https://cdn.example.com/logo/shop-d.png",
                    false
            );
      }

      private void createShopIfNotExists(String username, String name, String phone, String email,
                                         String address, String description, String logoUrl, boolean isMall) {
            // Tìm người dùng theo username
            Optional<AppUser> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                  throw new RuntimeException("Không tìm thấy người dùng với username: " + username);
            }

            AppUser user = userOpt.get();

            // Kiểm tra xem cửa hàng của người dùng đã tồn tại chưa
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
                  System.out.println("✅ Đã tạo cửa hàng: " + name + " cho username: " + username + " với user_id: " + user.getId());
            }
      }
}