package com.sope.sope_ecommerce_backend.seeder.user;

import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.entities.Role;
import com.sope.sope_ecommerce_backend.entities.UserRole;
import com.sope.sope_ecommerce_backend.enums.RoleName;
import com.sope.sope_ecommerce_backend.enums.UserStatus;
import com.sope.sope_ecommerce_backend.repositories.RoleRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class UserSeeder {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public void run() throws Exception {
        // Mỗi user có nhiều role để test
        createUserIfNotExists("admin", "Admin User", "admin@example.com", "admin123", Arrays.asList("ADMIN", "USER"));
        createUserIfNotExists("user", "Regular User", "user@example.com", "user123", Arrays.asList("USER", "SELLER"));
        createUserIfNotExists("seller", "Shop Seller", "seller@example.com", "seller123",
                Arrays.asList("SELLER", "USER"));
    }

    private void createUserIfNotExists(String username, String name, String email, String rawPassword,
            List<String> roleNames) {
        if (userRepository.findByUsername(username).isEmpty()) {

            AppUser appUser = AppUser.builder()
                    .username(username)
                    .name(name)
                    .email(email)
                    .password(passwordEncoder.encode(rawPassword))
                    .status(UserStatus.ACTIVE)
                    .build();

            // Thêm nhiều role
            roleNames.forEach(roleStr -> {
                RoleName roleName = RoleName.valueOf(roleStr.toUpperCase());
                Role role = roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

                UserRole userRole = UserRole.builder()
                        .user(appUser)
                        .role(role)
                        .grantedBy("SYSTEM")
                        .build();

                appUser.getUserRoles().add(userRole);
            });

            userRepository.save(appUser);
            System.out.println("✅ Seeded user: " + username + " with roles: " + roleNames);
        }
    }

}
