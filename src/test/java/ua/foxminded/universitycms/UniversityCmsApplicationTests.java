package ua.foxminded.universitycms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {UniversityCmsApplication.class})
@ActiveProfiles("test")
class UniversityCmsApplicationTests {

    @Test
    void contextLoads() {
    }

}
