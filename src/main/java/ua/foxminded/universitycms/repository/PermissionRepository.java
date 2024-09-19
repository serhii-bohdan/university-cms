package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Permission;

/**
 * Repository interface for managing {@link Permission} entities.
 * <p>
 * Provides standard CRUD (Create, Read, Update, Delete) operations for permissions through
 * the inherited {@link JpaRepository} interface.
 *
 * @author Serhii Bohdan
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
