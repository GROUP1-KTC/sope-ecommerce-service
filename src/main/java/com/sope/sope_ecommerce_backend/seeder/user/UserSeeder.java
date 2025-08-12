package com.sope.sope_ecommerce_backend.seeder.user;


import com.sope.sope_ecommerce_backend.entities.Role;
import com.sope.sope_ecommerce_backend.entities.User;
import com.sope.sope_ecommerce_backend.enums.RoleName;
import com.sope.sope_ecommerce_backend.repositories.RoleRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@Profile("local")
@AllArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createUserIfNotExists("admin", "Admin User", "admin@example.com", "admin123", "ADMIN");
        createUserIfNotExists("user", "Regular User", "user@example.com", "user123", "USER");
        createUserIfNotExists("seller", "Shop Seller", "seller@example.com", "seller123", "SELLER");
    }

    private void createUserIfNotExists(String username, String name, String email, String rawPassword, String roleNamestr) {
        if (userRepository.findByUsername(username).isEmpty()) {
            RoleName roleName = RoleName.valueOf(roleNamestr.toUpperCase());
            Optional<Role> roleOpt = roleRepository.findByRoleName(roleName);
            if (roleOpt.isEmpty()) {
                throw new RuntimeException("Role not found: " + roleName);
            }

            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setAddress("Default Address");
            user.setPhone("0123456789");
            user.setNote(null);
            user.setStatus("ACTIVE");
            user.setRoles(Collections.singleton(roleOpt.get()));

            userRepository.save(user);
            System.out.println("✅ Seeded user: " + username);
        }
    }
}
