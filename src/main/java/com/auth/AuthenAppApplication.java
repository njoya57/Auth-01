package com.auth;

import com.auth.entities.AppRole;
import com.auth.entities.AppUser;
import com.auth.service.AuthService;
import java.util.ArrayList;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AuthenAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenAppApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner start(AuthService authService) {
        return args -> {
            authService.saveUser(new AppUser(null, "admin", "1234", new ArrayList<>()));
            authService.saveUser(new AppUser(null, "user1", "1234", new ArrayList<>()));
            authService.saveUser(new AppUser(null, "user2", "1234", new ArrayList<>()));
            authService.saveUser(new AppUser(null, "user3", "1234", new ArrayList<>()));
            authService.saveUser(new AppUser(null, "user4", "1234", new ArrayList<>()));

            authService.saveRole(new AppRole(null, "ADMIN"));
            authService.saveRole(new AppRole(null, "USER"));
            authService.saveRole(new AppRole(null, "CUSTOMER_MANAGER"));
            authService.saveRole(new AppRole(null, "BILLS_MANAGER"));
            authService.saveRole(new AppRole(null, "PRODUCT_MANAGER"));

            authService.addUserToRole("admin", "ADMIN");
            authService.addUserToRole("admin", "USER");
            authService.addUserToRole("user1", "USER");
            authService.addUserToRole("user1", "CUSTOMER_MANAGER");
            authService.addUserToRole("user2", "USER");
            authService.addUserToRole("user2", "BILLS_MANAGER");
            authService.addUserToRole("user3", "USER");
            authService.addUserToRole("user3", "PRODUCT_MANAGER");
            authService.addUserToRole("user4", "USER");
            authService.addUserToRole("user4", "PRODUCT_MANAGER");

        };
    }

}
