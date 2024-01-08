package ua.foxminded.universitycms.service;

import java.util.Optional;
import ua.foxminded.universitycms.dto.TopicDto;

/**
 * The {@code TopicService} interface provides methods for managing topics.
 * <p>
 * This interface includes methods for adding a topic, getting a topic by ID,
 * and deleting a topic by ID.
 *
 * @author Serhii Bohdan
 */
public interface TopicService {

    /**
     * Adds a new topic.
     *
     * @param topicDto the topic DTO to add
     * @return true if the topic was added successfully, false otherwise
     */
    boolean addTopic(TopicDto topicDto);

    /**
     * Gets a topic by ID.
     *
     * @param topicId the ID of the topic to get
     * @return an Optional containing the topic DTO if found, an empty Optional
     *         otherwise
     */
    Optional<TopicDto> getTopicById(Long topicId);

    /**
     * Deletes a topic by ID.
     *
     * @param topicId the ID of the topic to delete
     * @return true if the topic was deleted successfully, false otherwise
     */
    boolean deleteTopicById(Long topicId);

}
