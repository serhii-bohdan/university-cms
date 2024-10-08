package ua.foxminded.universitycms.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Course;

/**
 * The {@code CourseRepository} interface is a Spring Data JPA repository for
 * {@link Course} entities.
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
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Finds courses with a name that matches the given name, ignoring case sensitivity.
     * The results are returned as a {@link Page} of courses, allowing for pagination
     * and sorting.
     *
     * @param name     the name to search for, ignoring case
     * @param pageable the pagination information, such as page number and size
     * @return a {@link Page} of matching courses, or an empty {@link Page} if none found
     */
    Page<Course> findCourseByCourseNameIgnoreCase(String name, Pageable pageable);

    /**
     * Finds courses authored by the specified teacher.
     *
     * @param authorId the ID of the author (teacher)
     * @return a list of courses authored by the specified teacher, or an empty list if none found
     */
    List<Course> findByAuthorId(long authorId);

}
