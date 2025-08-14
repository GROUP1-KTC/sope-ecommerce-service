package com.sope.sope_ecommerce_backend.seeder;

import com.sope.sope_ecommerce_backend.seeder.category.CategorySeeder;
import com.sope.sope_ecommerce_backend.seeder.user.RoleSeeder;
import com.sope.sope_ecommerce_backend.seeder.user.ShopSeeder;
import com.sope.sope_ecommerce_backend.seeder.user.UserSeeder;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private RoleSeeder roleSeeder;
    private UserSeeder userSeeder;
    private ShopSeeder shopSeeder;
    private CategorySeeder categorySeeder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Bắt đầu seed dữ liệu...");

        // 1. Seed roles
        roleSeeder.run();
        System.out.println("✅ Roles seeded");

        // 2. Seed users
        userSeeder.run();
        System.out.println("✅ Users seeded");

        // 3. Seed shops (dựa vào user vừa tạo)
        shopSeeder.run();
        System.out.println("✅ Shops seeded");

        // 4. Category seeder

        categorySeeder.run();
        System.out.println("Categoryss seeded");

        System.out.println("🎯 Hoàn tất seed dữ liệu");
    }

}