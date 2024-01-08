package ua.foxminded.universitycms.repository;

import java.util.Optional;
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
     * Finds a group by its name.
     *
     * @param groupName the name of the group to find
     * @return an Optional containing the found group, or an empty Optional if no
     *         group was found with the given name
     */
    Optional<Group> findByGroupName(String groupName);

}