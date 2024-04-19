package ua.foxminded.universitycms.service;

import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.model.Lesson;

/**
 * The {@code LessonService} interface defines a set of operations for managing {@link Lesson} entities and their
 * corresponding {@link LessonDto} representations. It extends the generic {@link Service} interface, providing
 * specialized services for working with lessons.
 *
 * @author Serhii Bohdan
 */
public interface LessonService extends Service<Lesson, LessonDto> {
}
