package ua.foxminded.universitycms.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.model.enumeration.RoleName;

/**
 * Spring Data JPA repository for managing {@link Role} entities in the university management system.
 * <p>
 * This interface extends {@link JpaRepository}, inheriting standard CRUD operations (create, read,
 * update, delete) for the {@link Role} entity, identified by a {@code Long} primary key. It also
 * provides a custom query method to retrieve a role by its name. The {@code @Repository} annotation
 * marks this interface as a Spring Data repository, enabling automatic implementation by Spring to
 * encapsulate storage, retrieval, and search behavior for role entities.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Role
 * @see RoleName
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Retrieves a role by its name.
     * <p>
     * This method queries the database for a {@link Role} entity matching the specified
     * {@link RoleName} value, returning an {@link Optional} to handle cases where no matching role
     * is found.
     *
     * @param roleName the name of the role to search for, as defined in the {@link RoleName} enumeration
     * @return an {@link Optional} containing the {@link Role} if found, or an empty {@link Optional}
     * if no role matches the provided name
     */
    Optional<Role> findByRoleName(RoleName roleName);

}
