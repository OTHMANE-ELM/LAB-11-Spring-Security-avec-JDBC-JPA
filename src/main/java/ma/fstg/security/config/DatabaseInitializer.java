package ma.fstg.security.config;

import ma.fstg.security.entities.Role;
import ma.fstg.security.entities.User;
import ma.fstg.security.repositories.RoleRepository;
import ma.fstg.security.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;

@Configuration
public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Bean
    CommandLineRunner init(RoleRepository roleRepo, UserRepository userRepo, BCryptPasswordEncoder encoder) {
        return args -> {
            logger.info("Initialisation de la base de données...");
            
            // Nettoyage existant pour éviter les doublons sur restart
            userRepo.deleteAll();
            roleRepo.deleteAll();
            
            Role adminRole = roleRepo.save(new Role(null, "ROLE_ADMIN"));
            Role userRole = roleRepo.save(new Role(null, "ROLE_USER"));

            User admin = new User(null, "admin", encoder.encode("1234"), true, List.of(adminRole, userRole));
            User user = new User(null, "user", encoder.encode("1111"), true, List.of(userRole));

            userRepo.saveAll(List.of(admin, user));
            logger.info("Données initiales chargées : admin/1234 et user/1111");
        };
    }
}
