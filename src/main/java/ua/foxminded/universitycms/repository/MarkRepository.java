package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Mark;
import ua.foxminded.universitycms.model.Topic;
import java.util.List;

/**
 * The {@code MarkRepository} interface is a Spring Data JPA repository for
 * {@link Mark} entities.
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
public interface MarkRepository extends JpaRepository<Mark, Long> {

    /**
     * Finds marks for a student enrolled in a specific course.
     * <p>
     * This method uses a custom JPQL query to achieve the desired result.
     * It joins the {@link Mark} entity with the associated {@link Topic} and filters
     * the results based on the provided student ID and course ID. The marks are
     * ordered by the topic order within the course.
     *
     * @param studentId the ID of the student to find marks for
     * @param courseId  the ID of the course to find marks within
     * @return a list of marks for the specified student and course, or an empty list if none found
     */
    @Query("SELECT m FROM Mark m JOIN m.topic t " +
           "WHERE m.student.id = :studentId AND t.course.id = :courseId " +
           "ORDER BY t.topicOrder")
    List<Mark> findMarksByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

}
