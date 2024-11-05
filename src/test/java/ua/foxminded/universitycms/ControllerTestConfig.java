package ua.foxminded.universitycms;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ua.foxminded.universitycms.repository.GroupRepository;
import ua.foxminded.universitycms.repository.StudentRepository;

@TestConfiguration
public class ControllerTestConfig {

    @MockBean
    private GroupRepository groupRepositoryMock;

    @MockBean
    private StudentRepository studentRepositoryMock;

    @Bean
    GroupRepository groupRepositoryMock() {
        return groupRepositoryMock;
    }

    @Bean
    StudentRepository studentRepositoryMock() {
        return studentRepositoryMock;
    }

}
