package ua.foxminded.universitycms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Student;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link Student} entities in the university management system.
 * <p>
 * This interface extends {@link JpaRepository}, inheriting standard CRUD operations (create, read,
 * update, delete) and pagination support for the {@link Student} entity, identified by a {@code Long}
 * primary key. It also provides custom query methods to retrieve students based on email, group, and
 * active status criteria. The {@code @Repository} annotation marks this interface as a Spring Data
 * repository, enabling automatic implementation by Spring to encapsulate storage, retrieval, and
 * search behavior for student entities.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Student
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Retrieves a student by their unique email address.
     * <p>
     * This method queries the database for a {@link Student} entity with the specified email address,
     * returning an {@link Optional} to handle cases where no matching student is found.
     *
     * @param email the email address of the student to search for
     * @return an {@link Optional} containing the student if found, or an empty {@link Optional} if no
     * student matches the provided email
     */
    Optional<Student> findByEmail(String email);

    /**
     * Retrieves a paginated list of students by their email address.
     * <p>
     * This method queries the database for {@link Student} entities matching the specified email
     * address, returning results as a {@link Page} object to support pagination and sorting. The
     * {@link Pageable} parameter defines the page size, page number, and sort options.
     *
     * @param email    the email address of the students to search for
     * @param pageable the pagination and sorting configuration
     * @return a {@link Page} containing matching students, or an empty page if no matches are found
     */
    Page<Student> findByEmail(String email, Pageable pageable);

    /**
     * Retrieves all students belonging to a specific group by its ID.
     * <p>
     * This method queries the database for {@link Student} entities associated with the specified
     * group ID, returning the results as a {@link List}.
     *
     * @param groupId the ID of the group whose students are to be retrieved
     * @return a {@link List} of students in the specified group, or an empty list if no students are found
     */
    List<Student> findByGroupId(Long groupId);

    /**
     * Retrieves a student by their email address and active status.
     * <p>
     * This method queries the database for a {@link Student} entity matching both the specified email
     * address and active status, returning an {@link Optional} to handle cases where no matching
     * student is found.
     *
     * @param email    the email address of the student to search for
     * @param isActive the active status of the student (true for active, false for inactive)
     * @return an {@link Optional} containing the student if found, or an empty {@link Optional} if no
     * student matches the provided email and active status
     */
    Optional<Student> findByEmailAndIsActive(String email, Boolean isActive);

}
