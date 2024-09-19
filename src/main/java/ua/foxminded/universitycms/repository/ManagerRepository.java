package ua.foxminded.universitycms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Manager;
import java.util.Optional;

/**
 * The {@code ManagerRepository} interface is a Spring Data JPA repository
 * for {@link Manager} entities.
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
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    /**
     * Finds a manager by their unique email address.
     *
     * @param email The email address of the manager to search for.
     * @return An {@link Optional} containing the manager if found, or an empty Optional if not.
     */
    Optional<Manager> findByEmail(String email);

    /**
     * Finds managers based on their first name and last name, ignoring case.
     * <p>
     * This method leverages Spring Data JPA's naming convention to derive a query from the method name.
     * It searches for managers whose first name and last name match the provided parameters, irrespective of case.
     *
     * @param firstName The first name to search for (case-insensitive).
     * @param lastName  The last name to search for (case-insensitive).
     * @param pageable  Pagination information for controlling the returned results.
     * @return A {@link Page} of managers matching the search criteria, possibly empty if none are found.
     */
    Page<Manager> findByName_FirstNameAndName_LastNameIgnoreCase(String firstName, String lastName, Pageable pageable);

}
