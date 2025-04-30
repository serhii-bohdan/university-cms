package ua.foxminded.universitycms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Manager;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link Manager} entities in the university management system.
 * <p>
 * This interface extends {@link JpaRepository}, inheriting standard CRUD operations (create, read,
 * update, delete) and pagination support for the {@link Manager} entity, identified by a {@code Long}
 * primary key. It also provides custom query methods to retrieve managers based on their email address.
 * The {@code @Repository} annotation marks this interface as a Spring Data repository, enabling automatic
 * implementation by Spring to encapsulate storage, retrieval, and search behavior for manager entities.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Manager
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    /**
     * Retrieves a manager by their unique email address.
     * <p>
     * This method queries the database for a {@link Manager} entity with the specified email address,
     * returning an {@link Optional} to handle cases where no matching manager is found.
     *
     * @param email the email address of the manager to search for
     * @return an {@link Optional} containing the manager if found, or an empty {@link Optional} if no
     * manager matches the provided email
     */
    Optional<Manager> findByEmail(String email);

    /**
     * Retrieves a paginated list of managers by their email address.
     * <p>
     * This method queries the database for {@link Manager} entities matching the specified email address,
     * returning results as a {@link Page} object to support pagination and sorting. The {@link Pageable}
     * parameter defines the page size, page number, and sort options.
     *
     * @param email    the email address of the managers to search for
     * @param pageable the pagination and sorting configuration
     * @return a {@link Page} containing the matching managers, or an empty page if no matches are found
     */
    Page<Manager> findByEmail(String email, Pageable pageable);

}
