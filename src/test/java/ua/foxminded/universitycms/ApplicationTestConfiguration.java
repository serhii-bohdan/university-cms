package ua.foxminded.universitycms;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.SecureRandom;

@TestConfiguration
public class ApplicationTestConfiguration {

    private static final BCryptVersion B_CRYPT_VERSION = BCryptPasswordEncoder.BCryptVersion.$2B;
    private static final int STRENGTH = 12;

    @Bean
    PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder(B_CRYPT_VERSION, STRENGTH, new SecureRandom());
    }

}
