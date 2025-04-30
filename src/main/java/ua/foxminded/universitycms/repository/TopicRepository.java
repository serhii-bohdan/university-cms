package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Topic;
import java.util.List;

/**
 * Spring Data JPA repository for managing {@link Topic} entities in the university management system.
 * <p>
 * This interface extends {@link JpaRepository}, inheriting standard CRUD operations (create, read,
 * update, delete) for the {@link Topic} entity, identified by a {@code Long} primary key. It also
 * provides a custom query method to retrieve topics based on their associated course. The
 * {@code @Repository} annotation marks this interface as a Spring Data repository, enabling automatic
 * implementation by Spring to encapsulate storage, retrieval, and search behavior for topic entities.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Topic
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    /**
     * Retrieves all topics associated with a specific course by its ID.
     * <p>
     * This method queries the database for {@link Topic} entities linked to the {@link Course} with
     * the specified ID, returning the results as a {@link List}.
     *
     * @param courseId the ID of the course whose topics are to be retrieved
     * @return a {@link List} of topics associated with the specified course, or an empty list if no
     * topics are found
     */
    List<Topic> findByCourseId(Long courseId);

}
