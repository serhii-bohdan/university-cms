package ua.foxminded.universitycms.repository;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.universitycms.model.Course;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
    CourseRepository.class}))
@ActiveProfiles({"test"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/clear_tables.sql",
    "/sql/students_courses_test_init.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseRepositoryTest {

    private static final Long STUDENT_ID = 1L;
    private static final Long STUDENT_COURSES_LIST_SIZE = 3L;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void findStudentCoursesByStudentId_shouldReturnListOfCoursesInWhichStudentIsEnrolled_whenStudentWithGivenIdExistAndStudentEnrolledInCourses() {
        List<Course> studentCourses = courseRepository.findStudentCoursesByStudentId(STUDENT_ID);

        assertFalse(studentCourses.isEmpty());
        assertEquals(STUDENT_COURSES_LIST_SIZE, studentCourses.size());
    }

    @Test
    @Sql("/sql/students_courses_table_clearing.sql")
    void findStudentCoursesByStudentId_shouldReturnEmptyCoursesList_whenStudentNotEnrolledInAnyCourse() {
        List<Course> studentCourses = courseRepository.findStudentCoursesByStudentId(STUDENT_ID);

        assertTrue(studentCourses.isEmpty());
    }


    @Test
    void findStudentCoursesByStudentId_shouldReturnEmptyCoursesList_whenStudentWithGivenIdDoesNotExist() {
        long studentIdWhichDoesNotExist = 2L;

        List<Course> studentCourses = courseRepository.findStudentCoursesByStudentId(studentIdWhichDoesNotExist);

        assertTrue(studentCourses.isEmpty());
    }

}
