package ua.foxminded.universitycms.service;

import ua.foxminded.universitycms.dto.TopicDto;
import ua.foxminded.universitycms.model.Topic;

/**
 * Service interface for managing {@link Topic} entities and their DTO representations in the university management
 * system.
 * <p>
 * This interface extends the generic {@link Service} interface, specializing in operations for {@link Topic} entities
 * mapped to {@link TopicDto} DTOs. It provides a framework for CRUD operations inherited from {@link Service} to
 * manage topics within courses, such as adding, retrieving, updating, and deleting topics. Implementations of this
 * interface handle business logic related to topic management, supporting extensible functionality as needed for
 * course structuring and academic tracking.
 *
 * @author Serhii Bohdan
 * @see Service
 * @see Topic
 * @see TopicDto
 */
public interface TopicService extends Service<Topic, TopicDto> {
}
