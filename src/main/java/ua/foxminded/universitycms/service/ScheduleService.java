package ua.foxminded.universitycms.service;

import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.model.Schedule;
import java.util.Optional;

/**
 * The {@code ScheduleService} interface defines a set of operations for managing {@link Schedule} entities and their
 * corresponding {@link ScheduleDto} representations. It extends the generic {@link Service} interface, providing
 * specialized services for working with schedules, including retrieving schedules for specific students.
 *
 * @author Serhii Bohdan
 */
public interface ScheduleService extends Service<Schedule, ScheduleDto> {

    /**
     * Retrieves the schedule for a given student.
     *
     * @param studentId the ID of the student whose schedule to retrieve
     * @return an {@link Optional} containing a {@link ScheduleDto} representing the student's schedule,
     * or an empty {@link Optional} if no schedule is found for the student
     */
    Optional<ScheduleDto> getScheduleForStudent(long studentId);

    /**
     * Retrieves the schedule for a given teacher.
     *
     * @param teacherId the ID of the teacher whose schedule to retrieve
     * @return an {@link Optional} containing a {@link ScheduleDto} representing the teacher's schedule,
     * or an empty {@link Optional} if no schedule is found for the teacher
     */
    Optional<ScheduleDto> getScheduleForTeacher(long teacherId);

}
