package ua.foxminded.universitycms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Lesson;

/**
 * The {@code LessonRepository} interface is a Spring Data JPA repository for
 * {@link Lesson} entities.
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
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    /**
     * Retrieves a list of lessons associated with a specific study day by its ID.
     *
     * @param studyDayId the ID of the study day for which lessons are to be retrieved
     * @return a list of lessons scheduled for the specified study day, or an empty list if none are found
     */
    List<Lesson> findLessonByStudyDayId(Long studyDayId);

}
