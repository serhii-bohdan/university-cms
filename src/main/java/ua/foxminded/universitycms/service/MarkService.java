package ua.foxminded.universitycms.service;

import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.model.Mark;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing {@link Mark} entities and their DTO representations in the university management
 * system.
 * <p>
 * This interface extends the generic {@link Service} interface, specializing in operations for {@link Mark} entities
 * mapped to {@link MarkDto} DTOs. It provides methods for CRUD operations inherited from {@link Service}, along with
 * additional functionality for retrieving marks based on student, course, and topic criteria, fetching topic names
 * within a course, and identifying unrated topics for a student in a course. Implementations of this interface handle
 * business logic related to mark management, facilitating academic performance tracking and evaluation.
 *
 * @author Serhii Bohdan
 * @see Service
 * @see Mark
 * @see MarkDto
 */
public interface MarkService extends Service<Mark, MarkDto> {

    /**
     * Retrieves a list of marks for a specific student within a course, filtered by topic name.
     * <p>
     * Fetches all marks associated with the specified {@code studentId} and {@code courseId}, further filtered by
     * the given {@code topicName}. This allows for targeted retrieval of a student's performance on a particular
     * topic within a course.
     *
     * @param studentId the ID of the student whose marks are to be retrieved
     * @param courseId  the ID of the course containing the marks
     * @param topicName the name of the topic to filter the marks by; may be null or empty for broader results
     *                  depending on implementation
     * @return a {@link List} of {@link MarkDto} objects representing the filtered marks
     */
    List<MarkDto> findStudentCourseMarksByTopicName(long studentId, long courseId, String topicName);

    /**
     * Retrieves the names of topics within a specified course.
     * <p>
     * Fetches a list of topic names associated with the course identified by {@code courseId}, useful for displaying
     * or selecting topics related to a course in the application.
     *
     * @param courseId the ID of the course for which to retrieve topic names
     * @return a {@link List} of topic names as strings
     */
    List<String> getNamesOfTopicsInCourse(long courseId);

    /**
     * Retrieves a mapping of unrated topics for a student within a specified course.
     * <p>
     * Identifies topics within the course specified by {@code courseId} that have not yet been rated for the student
     * identified by {@code studentId}. Returns a map where the keys are topic names and the values are their
     * corresponding topic IDs, aiding in tracking pending evaluations.
     *
     * @param studentId the ID of the student whose unrated topics are to be retrieved
     * @param courseId  the ID of the course containing the topics
     * @return a {@link Map} with topic names as keys and their IDs as values for unrated topics
     */
    Map<String, Long> getUnratedTopics(long studentId, long courseId);

}
