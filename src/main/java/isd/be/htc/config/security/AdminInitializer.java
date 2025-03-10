package isd.be.htc.config.security;

import isd.be.htc.model.User;
import isd.be.htc.model.enums.UserRole;
import isd.be.htc.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {
    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findUserByUserName("admin@gmail.com").isEmpty()) {
                User admin = new User();
                admin.setUserName("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("12345678"));
                admin.setRole(UserRole.ADMIN);
                userRepository.save(admin);
                System.out.println("Admin created! admin@gmail.com / 12345678");
            }
        };
    }
}
