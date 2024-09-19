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
import ua.foxminded.universitycms.model.Schedule;
import java.util.Optional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
    ScheduleRepository.class}))
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/clear_tables.sql",
    "/sql/schedule_test_init.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    void findTeacherScheduleByTeacherId_shouldOptionalWithSchedule_whenTeacherWithGivenIdHasSchedule() {
        Optional<Schedule> scheduleOptional = scheduleRepository.findTeacherScheduleByTeacherId(1L);

        assertTrue(scheduleOptional.isPresent());
        assertEquals(1, scheduleOptional.get().getId());
    }

    @Test
    void findTeacherScheduleByTeacherId_shouldEmptyOptional_whenTeacherWithGivenIdHasNotSchedule() {
        Optional<Schedule> scheduleOptional = scheduleRepository.findTeacherScheduleByTeacherId(2L);

        assertTrue(scheduleOptional.isEmpty());
    }

    @Test
    void findTeacherScheduleByTeacherId_shouldEmptyOptional_whenTeacherWithGivenIdNotExist() {
        Optional<Schedule> scheduleOptional = scheduleRepository.findTeacherScheduleByTeacherId(3L);

        assertTrue(scheduleOptional.isEmpty());
    }

    @Test
    @Sql("/sql/clear_tables.sql")
    void findTeacherScheduleByTeacherId_shouldEmptyOptional_whenTeachersAndSchedulesTablesAreEmpty() {
        Optional<Schedule> scheduleOptional = scheduleRepository.findTeacherScheduleByTeacherId(1L);

        assertTrue(scheduleOptional.isEmpty());
    }

    @Test
    void findStudentScheduleByStudentId_shouldOptionalWithSchedule_whenStudentWithGivenIdHasSchedule() {
        Optional<Schedule> scheduleOptional = scheduleRepository.findStudentScheduleByStudentId(1L);

        assertTrue(scheduleOptional.isPresent());
        assertEquals(2, scheduleOptional.get().getId());
    }

    @Test
    void findStudentScheduleByStudentId_shouldEmptyOptional_whenStudentWithGivenIdHasNotSchedule() {
        Optional<Schedule> scheduleOptional = scheduleRepository.findStudentScheduleByStudentId(2L);

        assertTrue(scheduleOptional.isEmpty());
    }

    @Test
    void findStudentScheduleByStudentId_shouldEmptyOptional_whenStudentWithGivenIdNotExist() {
        Optional<Schedule> scheduleOptional = scheduleRepository.findStudentScheduleByStudentId(3L);

        assertTrue(scheduleOptional.isEmpty());
    }

    @Test
    @Sql("/sql/clear_tables.sql")
    void findStudentScheduleByStudentId_shouldEmptyOptional_whenStudentsAndSchedulesTablesAreEmpty() {
        Optional<Schedule> scheduleOptional = scheduleRepository.findStudentScheduleByStudentId(1L);

        assertTrue(scheduleOptional.isEmpty());
    }

}
