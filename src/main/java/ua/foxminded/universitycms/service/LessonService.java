package ua.foxminded.universitycms.service;

import java.util.Map;
import jakarta.validation.constraints.NotNull;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.model.Lesson;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;

/**
 * The {@code LessonService} interface defines a set of operations for managing {@link Lesson} entities and their
 * corresponding {@link LessonDto} representations. It extends the generic {@link Service} interface, providing
 * specialized services for working with lessons.
 *
 * @author Serhii Bohdan
 */
public interface LessonService extends Service<Lesson, LessonDto> {

    /**
     * Retrieves a mapping of course names to their respective IDs for the given user.
     *
     * @param customUserDetails the authenticated user details, must not be null
     * @return a map where keys are course names and values are their corresponding IDs
     */
    Map<String, Long> getUserCourses(@NotNull CustomUserDetails customUserDetails);

}
