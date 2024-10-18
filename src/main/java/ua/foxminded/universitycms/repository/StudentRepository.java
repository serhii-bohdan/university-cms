package ua.foxminded.universitycms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Student;
import java.util.List;
import java.util.Optional;

/**
 * The {@code StudentRepository} interface is a Spring Data JPA repository for
 * {@link Student} entities.
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
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Finds a student by their unique email address.
     *
     * @param email The email address of the student to search for.
     * @return An {@link Optional} containing the student if found, or an empty Optional if not.
     */
    Optional<Student> findByEmail(String email);

    /**
     * Finds students with a matching first name and last name (case-insensitive).
     * This method uses the Spring Data JPA convention for named queries based on
     * method names.The results are returned as a {@link Page} of students, allowing
     * for pagination and sorting.
     *
     * @param firstName the first name to search for, ignoring case
     * @param lastName  the last name to search for, ignoring case
     * @param pageable  the pagination information, such as page number and size
     * @return a {@link Page} of matching students, or an empty {@link Page} if none found
     */
    Page<Student> findByName_FirstNameAndName_LastNameIgnoreCase(String firstName, String lastName, Pageable pageable);

    /**
     * Finds a list of students belonging to a specific group by its ID.
     *
     * @param groupId The unique identifier of the group to search for students in.
     * @return a list of {@link Student} objects that belong to the specified group,
     * or an empty list if no students are found in that group.
     */
    List<Student> findByGroupId(Long groupId);

}
