package ua.foxminded.universitycms;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ua.foxminded.universitycms.repository.*;

@TestConfiguration
public class ControllerTestConfig {

    @MockBean
    private GroupRepository groupRepositoryMock;

    @MockBean
    private StudentRepository studentRepositoryMock;

    @MockBean
    private TeacherRepository teacherRepositoryMock;

    @MockBean
    private ManagerRepository managerRepositoryMock;

    @MockBean
    private AdminRepository adminRepositoryMock;

    @MockBean
    private CourseRepository courseRepositoryMock;

    @MockBean
    private TopicRepository topicRepositoryMock;

    @MockBean
    private StudyDayRepository studyDayRepositoryMock;

    @MockBean
    private LessonRepository lessonRepositoryMock;

    @Bean
    GroupRepository groupRepositoryMock() {
        return groupRepositoryMock;
    }

    @Bean
    StudentRepository studentRepositoryMock() {
        return studentRepositoryMock;
    }

    @Bean
    CourseRepository courseRepositoryMock() {
        return courseRepositoryMock;
    }

    @Bean
    TopicRepository topicRepositoryMock() {
        return topicRepositoryMock;
    }

    @Bean
    TeacherRepository teacherRepositoryMock() {
        return teacherRepositoryMock;
    }

    @Bean
    ManagerRepository managerRepositoryMock() {
        return managerRepositoryMock;
    }

    @Bean
    AdminRepository adminRepositoryMock() {
        return adminRepositoryMock;
    }

    @Bean
    StudyDayRepository studyDayRepositoryMock() {
        return studyDayRepositoryMock;
    }

    @Bean
    LessonRepository lessonRepositoryMock() {
        return lessonRepositoryMock;
    }

}
