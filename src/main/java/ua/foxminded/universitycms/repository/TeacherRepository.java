package ua.foxminded.universitycms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Teacher;

/**
 * The {@code TeacherRepository} interface is a Spring Data JPA repository for
 * {@link Teacher} entities.
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
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    /**
     * Finds teachers with a matching first name and last name (case-insensitive).
     * This method leverages Spring Data JPA convention for creating named queries
     * based on method names. The results are returned as a Page of teachers, allowing
     * for pagination and sorting.
     *
     * @param firstName  the first name to search for, ignoring case
     * @param lastName   the last name to search for, ignoring case
     * @param pageable   the pagination information, such as page number and size
     * @return a {@link Page} of matching teachers, or an empty Page if none found
     */
    Page<Teacher> findByName_FirstNameAndName_LastNameIgnoreCase(String firstName, String lastName, Pageable pageable);

}
