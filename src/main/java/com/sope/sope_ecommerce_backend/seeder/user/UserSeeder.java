package com.sope.sope_ecommerce_backend.seeder.user;

import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.entities.Role;
import com.sope.sope_ecommerce_backend.entities.UserRole;
import com.sope.sope_ecommerce_backend.enums.RoleName;
import com.sope.sope_ecommerce_backend.repositories.RoleRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@Profile("local")
@AllArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUserIfNotExists("admin", "Admin User", "admin@example.com", "admin123", "ADMIN");
        createUserIfNotExists("user", "Regular User", "user@example.com", "user123", "USER");
        createUserIfNotExists("seller", "Shop Seller", "seller@example.com", "seller123", "SELLER");
    }

    private void createUserIfNotExists(String username, String name, String email, String rawPassword, String roleNamestr) {
        if (userRepository.findByUsername(username).isEmpty()) {
            RoleName roleName = RoleName.valueOf(roleNamestr.toUpperCase());
            Role role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

            AppUser appUser = AppUser.builder()
                    .username(username)
                    .name(name)
                    .email(email)
                    .password(passwordEncoder.encode(rawPassword))
                    .address("Default Address")
                    .phone("0123456789")
                    .status("ACTIVE")
                    .build();

            UserRole userRole = UserRole.builder()
                    .user(appUser)
                    .role(role)
                    .grantedBy("SYSTEM")
                    .build();

            appUser.getUserRoles().add(userRole);

            userRepository.save(appUser);

            System.out.println("✅ Seeded user: " + username);
        }
    }

}
