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
            if (userRepository.findUserByUserName("admin").isEmpty()) {
                User admin = new User();
                admin.setUserName("admin");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setRole(UserRole.ADMIN);
                userRepository.save(admin);
                System.out.println("Admin created! admin / 123456");
            }
        };
    }
}
