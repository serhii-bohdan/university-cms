package ua.foxminded.universitycms;

import java.security.SecureRandom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class provides Spring configuration for the application.
 * It scans for components within the "ua.foxminded.universitycms" package and
 * declares beans for essential services.
 *
 * @author Serhii Bohdan
 */
@Configuration
@ComponentScan(basePackages = "ua.foxminded.universitycms")
public class ApplicationConfiguration {

    /**
     * The BCrypt version to use for password encoding.
     */
    private static final BCryptVersion B_CRYPT_VERSION = BCryptVersion.$2B;

    /**
     * The strength (work factor) for password encoding.
     */
    private static final int STRENGTH = 12;

    /**
     * Creates a {@link PasswordEncoder} bean using BCrypt with the specified version and strength.
     *
     * @return a {@link BCryptPasswordEncoder} instance with configured settings
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(B_CRYPT_VERSION, STRENGTH, new SecureRandom());
    }

}
