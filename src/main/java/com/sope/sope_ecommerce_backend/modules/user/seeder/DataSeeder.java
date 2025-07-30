package com.sope.sope_ecommerce_backend.modules.user.seeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {
    @Autowired
    private RoleSeeder roleSeeder;
    @Autowired
    private UserSeeder userSeeder;

    @Override
    public void run(String... args) throws Exception {
        userSeeder.run(args);
        roleSeeder.run(args);
    }
}
