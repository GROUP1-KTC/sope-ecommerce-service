package com.sope.sope_ecommerce_backend.seeder.user;

import com.sope.sope_ecommerce_backend.entities.RoleEntity;
import com.sope.sope_ecommerce_backend.entities.UserEntity;
import com.sope.sope_ecommerce_backend.repositories.RoleRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.util.Collections;
import java.util.Optional;

@Component
@Profile("dev")
public class UserSeeder implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUserIfNotExists("admin", "Admin User", "admin@example.com", "admin123", "ROLE_ADMIN");
        createUserIfNotExists("user", "Regular User", "user@example.com", "user123", "ROLE_USER");
    }

    private void createUserIfNotExists(String username, String name, String email, String rawPassword, String roleName) {
        if (userRepository.findByUsername(username).isEmpty()) {
            Optional<RoleEntity> roleOpt = roleRepository.findByName(roleName);
            if (roleOpt.isEmpty()) {
                throw new RuntimeException("Role not found: " + roleName);
            }

            UserEntity user = new UserEntity();
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