package ua.foxminded.universitycms.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.universitycms.model.Mark;
import java.util.List;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
    MarkRepository.class}))
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/clear_tables.sql",
    "/sql/marks_test_init.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MarkRepositoryTest {

    @Autowired
    private MarkRepository markRepository;

    @Test
    void findMarksByStudentIdAndCourseId_shouldEmptyMarksList_whenStudentIdIsNull() {
        List<Mark> marks = markRepository.findMarksByStudentIdAndCourseId(null, 1L);

        assertTrue(marks.isEmpty());
    }

    @Test
    void findMarksByStudentIdAndCourseId_shouldEmptyMarksList_whenCourseIdIsNull() {
        List<Mark> marks = markRepository.findMarksByStudentIdAndCourseId(1L, null);

        assertTrue(marks.isEmpty());
    }

    @Test
    void findMarksByStudentIdAndCourseId_shouldEmptyMarksList_whenNoStudentWithGivenId() {
        List<Mark> marks = markRepository.findMarksByStudentIdAndCourseId(3L, 1L);

        assertTrue(marks.isEmpty());
    }

    @Test
    void findMarksByStudentIdAndCourseId_shouldEmptyMarksList_whenNoCourseWithGivenId() {
        List<Mark> marks = markRepository.findMarksByStudentIdAndCourseId(1L, 4L);

        assertTrue(marks.isEmpty());
    }

    @Test
    @Sql("/sql/clear_tables.sql")
    void findMarksByStudentIdAndCourseId_shouldEmptyMarksList_whenMarksTableIsEmpty() {
        List<Mark> marks = markRepository.findMarksByStudentIdAndCourseId(1L, 1L);

        assertTrue(marks.isEmpty());
    }

    @Test
    void findMarksByStudentIdAndCourseId_shouldNotEmptyMarksList_whenMarksWithGivenStudentIdAndCourseIdArePresent() {
        List<Mark> marks = markRepository.findMarksByStudentIdAndCourseId(1L, 1L);

        assertFalse(marks.isEmpty());
        assertEquals(6, marks.size());
    }

}
