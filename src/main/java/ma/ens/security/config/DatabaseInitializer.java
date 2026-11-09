package ma.ens.security.config;

import ma.ens.security.entities.Role;
import ma.ens.security.entities.User;
import ma.ens.security.repositories.RoleRepository;
import ma.ens.security.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer {

    @Bean
    CommandLineRunner init(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            Role adminRole = roleRepo.findByName("ROLE_ADMIN");
            if (adminRole == null) {
                adminRole = roleRepo.save(new Role(null, "ROLE_ADMIN"));
            }
            Role userRole = roleRepo.findByName("ROLE_USER");
            if (userRole == null) {
                userRole = roleRepo.save(new Role(null, "ROLE_USER"));
            }

            if (userRepo.findByUsername("admin").isEmpty()) {
                userRepo.save(new User(null, "admin", encoder.encode("1234"), true,
                        new ArrayList<>(List.of(adminRole, userRole))));
            }
            if (userRepo.findByUsername("user").isEmpty()) {
                userRepo.save(new User(null, "user", encoder.encode("1111"), true,
                        new ArrayList<>(List.of(userRole))));
            }
        };
    }
}
