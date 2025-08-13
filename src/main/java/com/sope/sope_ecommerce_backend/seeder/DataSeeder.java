package com.sope.sope_ecommerce_backend.seeder;

import com.sope.sope_ecommerce_backend.seeder.user.RoleSeeder;
import com.sope.sope_ecommerce_backend.seeder.user.UserSeeder;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private RoleSeeder roleSeeder;
    private UserSeeder userSeeder;

    @Override
    public void run(String... args) throws Exception {
        roleSeeder.run(args);
        userSeeder.run(args);
    }
}