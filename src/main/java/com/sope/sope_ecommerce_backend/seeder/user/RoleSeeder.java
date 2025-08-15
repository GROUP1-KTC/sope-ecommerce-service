package com.sope.sope_ecommerce_backend.seeder.user;

import com.sope.sope_ecommerce_backend.entities.Role;
import com.sope.sope_ecommerce_backend.enums.RoleName;
import com.sope.sope_ecommerce_backend.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoleSeeder {
    private RoleRepository roleRepository;

    public void run() throws Exception {
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("USER");
        createRoleIfNotExists("SELLER");
    }

    private void createRoleIfNotExists(String roleNameStr) {
        RoleName roleName = RoleName.valueOf(roleNameStr.toUpperCase());
        if (roleRepository.findByRoleName(roleName).isEmpty()) {
            Role role = new Role(roleName);
            roleRepository.save(role);
            System.out.println("✅ Seeded role: " + roleName);
        }
    }
}