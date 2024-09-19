package ua.foxminded.universitycms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Group;

/**
 * The {@code GroupRepository} interface is a Spring Data JPA repository for
 * {@link Group} entities.
 * <p>
 * This interface extends {@link JpaRepository}, which provides JPA related
 * methods such as save(), findOne(), findAll(), count(), delete(). This
 * interface is annotated with {@code @Repository}, indicating that it's a
 * "Repository" bean. A Repository is a mechanism for encapsulating storage,
 * retrieval, and search behavior which emulates a collection of objects. In
 * addition to the methods inherited from {@code JpaRepository}, this interface
 * also declares a method to find a group by its name.
 *
 * @author Serhii Bohdan
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    /**
     * Finds groups with a name that matches the given name, ignoring case sensitivity.
     * The results are returned as a {@link Page} of groups, allowing for pagination
     * and sorting.
     *
     * @param groupName  the name to search for, ignoring case
     * @param pageable   the pagination information, such as page number and size
     * @return a {@link Page} of matching groups, or an empty {@link Page} if none found
     */
    Page<Group> findByGroupNameIgnoreCase(String groupName, Pageable pageable);

}
