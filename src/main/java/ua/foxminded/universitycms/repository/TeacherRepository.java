package ua.foxminded.universitycms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Teacher;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link Teacher} entities in the university management system.
 * <p>
 * This interface extends {@link JpaRepository}, inheriting standard CRUD operations (create, read,
 * update, delete) and pagination support for the {@link Teacher} entity, identified by a {@code Long}
 * primary key. It also provides custom query methods to retrieve teachers based on email and active
 * status criteria. The {@code @Repository} annotation marks this interface as a Spring Data repository,
 * enabling automatic implementation by Spring to encapsulate storage, retrieval, and search behavior
 * for teacher entities.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Teacher
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    /**
     * Retrieves a teacher by their unique email address.
     * <p>
     * This method queries the database for a {@link Teacher} entity with the specified email address,
     * returning an {@link Optional} to handle cases where no matching teacher is found.
     *
     * @param email the email address of the teacher to search for
     * @return an {@link Optional} containing the teacher if found, or an empty {@link Optional} if no
     * teacher matches the provided email
     */
    Optional<Teacher> findByEmail(String email);

    /**
     * Retrieves a paginated list of teachers by their email address.
     * <p>
     * This method queries the database for {@link Teacher} entities matching the specified email
     * address, returning results as a {@link Page} object to support pagination and sorting. The
     * {@link Pageable} parameter defines the page size, page number, and sort options.
     *
     * @param email    the email address of the teachers to search for
     * @param pageable the pagination and sorting configuration
     * @return a {@link Page} containing matching teachers, or an empty page if no matches are found
     */
    Page<Teacher> findByEmail(String email, Pageable pageable);

    /**
     * Retrieves a teacher by their email address and active status.
     * <p>
     * This method queries the database for a {@link Teacher} entity matching both the specified email
     * address and active status, returning an {@link Optional} to handle cases where no matching
     * teacher is found.
     *
     * @param email    the email address of the teacher to search for
     * @param isActive the active status of the teacher (true for active, false for inactive)
     * @return an {@link Optional} containing the teacher if found, or an empty {@link Optional} if no
     * teacher matches the provided email and active status
     */
    Optional<Teacher> findByEmailAndIsActive(String email, Boolean isActive);

}
