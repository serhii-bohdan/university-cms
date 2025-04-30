package ua.foxminded.universitycms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Admin;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link Admin} entities in the university management system.
 * <p>
 * This interface extends {@link JpaRepository}, inheriting standard CRUD operations (create, read,
 * update, delete) and pagination support for the {@link Admin} entity, identified by a {@code Long}
 * primary key. It also defines custom query methods to retrieve admins based on specific criteria,
 * such as their email address. The {@code @Repository} annotation marks this interface as a Spring
 * Data repository, enabling automatic implementation by Spring.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Admin
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * Retrieves an admin by their email address.
     * <p>
     * This method queries the database for an {@link Admin} entity with the specified email address,
     * returning an {@link Optional} to handle cases where no matching admin is found.
     *
     * @param email the email address of the admin to search for
     * @return an {@link Optional} containing the admin if found, or an empty {@link Optional} if no
     * admin matches the provided email
     */
    Optional<Admin> findByEmail(String email);

    /**
     * Retrieves a paginated list of admins by their email address.
     * <p>
     * This method queries the database for {@link Admin} entities matching the specified email address,
     * returning results as a {@link Page} object to support pagination. The {@link Pageable} parameter
     * defines the page size, page number, and sorting options.
     *
     * @param email    the email address of the admins to search for
     * @param pageable the pagination and sorting configuration
     * @return a {@link Page} containing the matching admins, or an empty page if no matches are found
     */
    Page<Admin> findByEmail(String email, Pageable pageable);

}
