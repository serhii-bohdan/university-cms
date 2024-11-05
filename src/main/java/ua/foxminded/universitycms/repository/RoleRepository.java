package ua.foxminded.universitycms.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.model.enumeration.RoleName;

/**
 * Repository interface for managing {@link Role} entities.
 * <p>
 * Provides standard CRUD (Create, Read, Update, Delete) operations for roles through the
 * inherited {@link JpaRepository} interface.
 *
 * @author Serhii Bohdan
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a {@link Role} entity by its role name.
     *
     * @param roleName the name of the role to search for
     * @return an {@link Optional} containing the {@link Role} if found, or empty if not found
     */
    Optional<Role> findByRoleName(RoleName roleName);

}
