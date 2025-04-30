package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Mark;
import ua.foxminded.universitycms.model.Topic;
import java.util.List;

/**
 * Spring Data JPA repository for managing {@link Mark} entities in the university management system.
 * <p>
 * This interface extends {@link JpaRepository}, inheriting standard CRUD operations (create, read,
 * update, delete) for the {@link Mark} entity, identified by a {@code Long} primary key. It also
 * provides a custom query method to retrieve marks based on student and course criteria. The
 * {@code @Repository} annotation marks this interface as a Spring Data repository, enabling automatic
 * implementation by Spring to encapsulate storage, retrieval, and search behavior for mark entities.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mark
 * @see Topic
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {

    /**
     * Retrieves all marks for a student within a specific course, ordered by topic sequence.
     * <p>
     * This method executes a custom JPQL query that joins the {@link Mark} entity with its associated
     * {@link Topic} entity, filtering results by the specified student ID and course ID. The returned
     * marks are sorted by the {@code topicOrder} field of the {@link Topic} to reflect the progression
     * of topics within the course.
     *
     * @param studentId the ID of the student whose marks are to be retrieved
     * @param courseId  the ID of the course for which marks are to be retrieved
     * @return a {@link List} of marks for the specified student and course, ordered by topic order,
     * or an empty list if no marks are found
     */
    @Query("SELECT m FROM Mark m JOIN m.topic t " +
        "WHERE m.student.id = :studentId AND t.course.id = :courseId " +
        "ORDER BY t.topicOrder")
    List<Mark> findMarksByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

}
