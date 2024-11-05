package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Topic;
import java.util.List;

/**
 * The {@code TopicRepository} interface is a Spring Data JPA repository for
 * {@link Topic} entities.
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
public interface TopicRepository extends JpaRepository<Topic, Long> {

    /**
     * Finds topics that belong to the course with the given ID.
     *
     * @param courseId the ID of the course to search for topics within
     * @return a list of topics associated with the specified course,
     * or an empty list if none found
     */
    List<Topic> findByCourseId(Long courseId);

}
