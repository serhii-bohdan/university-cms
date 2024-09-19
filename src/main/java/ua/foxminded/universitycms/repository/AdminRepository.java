package ua.foxminded.universitycms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Admin;
import java.util.Optional;

/**
 * The {@code AdminRepository} interface is a Spring Data JPA repository for managing
 * {@link Admin} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing standard CRUD operations
 * (create, read, update, delete) and additional query methods specific to the Admin entity.
 *
 * @author Serhii Bohdan
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * Finds an admin by their email address.
     *
     * @param email The email address of the admin to search for.
     * @return An {@link Optional} containing the admin if found, or an empty Optional if not found.
     */
    Optional<Admin> findByEmail(String email);

    /**
     * Finds admins with a matching first name and last name, ignoring case sensitivity.
     *
     * <p>This method uses the Spring Data JPA naming convention to automatically generate a query
     * that searches for admins by their first and last name in the "admins" table.
     *
     * @param firstName The first name to search for (case-insensitive).
     * @param lastName  The last name to search for (case-insensitive).
     * @param pageable  The pagination information for controlling the returned results.
     * @return A {@link Page} containing matching admins, possibly empty if none are found.
     */
    Page<Admin> findByName_FirstNameAndName_LastNameIgnoreCase(String firstName, String lastName, Pageable pageable);

}
