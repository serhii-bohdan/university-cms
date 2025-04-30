package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Teacher;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link Schedule} entities in the university management system.
 * <p>
 * This interface extends {@link JpaRepository}, inheriting standard CRUD operations (create, read,
 * update, delete) for the {@link Schedule} entity, identified by a {@code Long} primary key. It also
 * provides custom query methods to retrieve schedules associated with specific teachers or students.
 * The {@code @Repository} annotation marks this interface as a Spring Data repository, enabling
 * automatic implementation by Spring to encapsulate storage, retrieval, and search behavior for
 * schedule entities.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Schedule
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * Retrieves the schedule associated with a specific teacher by their ID.
     * <p>
     * This method executes a custom JPQL query that joins the {@link Teacher} entity with its
     * associated {@link Schedule} entity, returning the schedule for the teacher with the specified
     * ID as an {@link Optional} to handle cases where no schedule is found.
     *
     * @param teacherId the ID of the teacher whose schedule is to be retrieved
     * @return an {@link Optional} containing the teacher's schedule, or an empty {@link Optional} if
     * no schedule is associated with the specified teacher
     */
    @Query("SELECT s FROM Teacher t JOIN t.schedule s WHERE t.id = :teacherId")
    Optional<Schedule> findTeacherScheduleByTeacherId(@Param("teacherId") long teacherId);

    /**
     * Retrieves the schedule associated with a specific student by their ID.
     * <p>
     * This method executes a custom JPQL query that joins the {@link Student} entity with its
     * associated {@link Schedule} entity, returning the schedule for the student with the specified
     * ID as an {@link Optional} to handle cases where no schedule is found.
     *
     * @param studentId the ID of the student whose schedule is to be retrieved
     * @return an {@link Optional} containing the student's schedule, or an empty {@link Optional} if
     * no schedule is associated with the specified student
     */
    @Query("SELECT sch FROM Student std JOIN std.schedule sch WHERE std.id = :studentId")
    Optional<Schedule> findStudentScheduleByStudentId(@Param("studentId") long studentId);

}
