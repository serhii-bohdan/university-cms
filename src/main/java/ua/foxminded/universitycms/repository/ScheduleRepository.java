package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Schedule;
import java.util.Optional;

/**
 * The {@code ScheduleRepository} interface is a Spring Data JPA repository for
 * {@link Schedule} entities.
 * <p>
 * This interface extends {@link JpaRepository}, which provides JPA related
 * methods such as save(), findOne(), findAll(), count(), delete(). This
 * interface is annotated with {@code @Repository}, indicating that it's a
 * "Repository" bean. A Repository is a mechanism for encapsulating storage,
 * retrieval, and search behavior which emulates a collection of objects.
 *
 * @author Serhii Bohdan
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * Retrieves the schedule associated with a specific teacher.
     *
     * @param teacherId The ID of the teacher.
     * @return An {@link Optional} containing the teacher's schedule, or an empty Optional if not found.
     */
    @Query("SELECT s FROM Teacher t JOIN t.schedule s WHERE t.id = :teacherId")
    Optional<Schedule> findTeacherScheduleByTeacherId(@Param("teacherId") long teacherId);

    /**
     * Retrieves the schedule associated with a specific student.
     *
     * @param studentId The ID of the student.
     * @return An {@link Optional} containing the student's schedule, or an empty Optional if not found.
     */
    @Query("SELECT sch FROM Student std JOIN std.schedule sch WHERE std.id = :studentId")
    Optional<Schedule> findStudentScheduleByStudentId(@Param("studentId") long studentId);

}
