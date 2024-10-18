package ua.foxminded.universitycms;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.foxminded.universitycms.repository.GroupRepository;
import java.security.SecureRandom;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

    private static final BCryptVersion B_CRYPT_VERSION = BCryptPasswordEncoder.BCryptVersion.$2B;
    private static final int STRENGTH = 12;

    @MockBean
    private GroupRepository groupRepositoryMock;

    @Bean
    PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder(B_CRYPT_VERSION, STRENGTH, new SecureRandom());
    }

    @Bean
    GroupRepository groupRepositoryMock() {
        return groupRepositoryMock;
    }

}
