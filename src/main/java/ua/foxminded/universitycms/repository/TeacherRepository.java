package ua.foxminded.universitycms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Teacher;
import java.util.Optional;

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
     * Finds a teacher by their unique email address.
     *
     * @param email The email address of the teacher to search for.
     * @return An {@link Optional} containing the teacher if found, or an empty Optional if not.
     */
    Optional<Teacher> findByEmail(String email);

    /**
     * Retrieves a page of teachers filtered by their email address.
     *
     * @param email    the email address to filter teachers by.
     * @param pageable the {@link Pageable} object specifying pagination and sorting information.
     * @return a {@link Page} of teachers matching the email filter.
     */
    Page<Teacher> findByEmail(String email, Pageable pageable);

    /**
     * Finds a teacher by their email address and active status.
     *
     * @param email    the email address of the teacher to search for.
     * @param isActive the active status to filter the teacher by.
     * @return an {@link Optional} containing the teacher if found, or an empty {@code Optional} if not.
     */
    Optional<Teacher> findByEmailAndIsActive(String email, Boolean isActive);

}
