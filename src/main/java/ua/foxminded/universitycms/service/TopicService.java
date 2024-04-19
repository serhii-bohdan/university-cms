package ua.foxminded.universitycms.service;

import ua.foxminded.universitycms.dto.TopicDto;
import ua.foxminded.universitycms.model.Topic;

/**
 * The {@code TopicService} interface defines a set of operations for managing {@link Topic} entities
 * and their corresponding {@link TopicDto} representations. It extends the generic {@link Service} interface,
 * providing specialized services for managing topics within a course. This includes methods for adding and
 * updating topics, but may be extended further depending on the specific needs of the system.
 *
 * @author Serhii Bohdan
 */
public interface TopicService extends Service<Topic, TopicDto> {
}
