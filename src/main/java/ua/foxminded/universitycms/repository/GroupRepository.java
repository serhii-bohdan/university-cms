package ua.foxminded.universitycms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Group;

/**
 * Spring Data JPA repository for managing {@link Group} entities in the university management system.
 * <p>
 * This interface extends {@link JpaRepository}, inheriting standard CRUD operations (create, read,
 * update, delete) and pagination support for the {@link Group} entity, identified by a {@code Long}
 * primary key. It also provides a custom query method to retrieve groups based on their name. The
 * {@code @Repository} annotation marks this interface as a Spring Data repository, enabling automatic
 * implementation by Spring to encapsulate storage, retrieval, and search behavior for group entities.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Group
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    /**
     * Retrieves groups with a name matching the specified value, ignoring case sensitivity.
     * <p>
     * This method queries the database for {@link Group} entities where the group name matches the
     * provided value (case-insensitive), returning results as a {@link Page} object to support
     * pagination and sorting. The {@link Pageable} parameter defines the page size, page number, and
     * sort options.
     *
     * @param groupName the group name to search for, ignoring case
     * @param pageable  the pagination and sorting configuration
     * @return a {@link Page} containing matching groups, or an empty page if no matches are found
     */
    Page<Group> findByGroupNameIgnoreCase(String groupName, Pageable pageable);

}
