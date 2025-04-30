package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Permission;

/**
 * Spring Data JPA repository for managing {@link Permission} entities in the university management system.
 * <p>
 * This interface extends {@link JpaRepository}, inheriting standard CRUD operations (create, read,
 * update, delete) for the {@link Permission} entity, identified by a {@code Long} primary key. The
 * {@code @Repository} annotation marks this interface as a Spring Data repository, enabling automatic
 * implementation by Spring to encapsulate storage, retrieval, and search behavior for permission
 * entities.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Permission
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
