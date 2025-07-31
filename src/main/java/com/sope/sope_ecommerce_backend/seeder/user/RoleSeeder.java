package com.sope.sope_ecommerce_backend.seeder.user;

import com.sope.sope_ecommerce_backend.entities.RoleEntity;
import com.sope.sope_ecommerce_backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class RoleSeeder implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotExists("ROLE_USER");
        createRoleIfNotExists("ROLE_ADMIN");
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            RoleEntity role = new RoleEntity();
            role.setName(roleName);
            roleRepository.save(role);
            System.out.println("✅ Seeded role: " + roleName);
        }
    }
}