package ua.foxminded.universitycms.service;

import java.util.Optional;
import ua.foxminded.universitycms.dto.LessonDto;

/**
 * The {@code LessonService} interface provides methods for managing lessons.
 * <p>
 * This interface includes methods for adding a lesson, getting a lesson by ID,
 * and deleting a lesson by ID.
 * 
 * @author Serhii Bohdan
 */
public interface LessonService {

    /**
     * Adds a new lesson.
     *
     * @param lessonDto the lesson DTO to add
     * @return true if the lesson was added successfully, false otherwise
     */
    boolean addLesson(LessonDto lessonDto);

    /**
     * Gets a lesson by ID.
     *
     * @param lessonId the ID of the lesson to get
     * @return an Optional containing the lesson DTO if found, an empty Optional
     *         otherwise
     */
    Optional<LessonDto> getLessonById(Long lessonId);

    /**
     * Deletes a lesson by ID.
     *
     * @param lessonId the ID of the lesson to delete
     * @return true if the lesson was deleted successfully, false otherwise
     */
    boolean deleteLessonById(Long lessonId);

}
