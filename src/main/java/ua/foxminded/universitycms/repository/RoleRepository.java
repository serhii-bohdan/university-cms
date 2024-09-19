package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Role;

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
}
