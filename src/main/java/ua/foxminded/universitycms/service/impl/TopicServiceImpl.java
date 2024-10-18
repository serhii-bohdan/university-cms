package ua.foxminded.universitycms.service.impl;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.TopicDto;
import ua.foxminded.universitycms.exception.ValidationException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Topic;
import ua.foxminded.universitycms.repository.TopicRepository;
import ua.foxminded.universitycms.service.TopicService;

/**
 * The {@code TopicServiceImpl} class implements the {@link TopicService} interface, providing concrete
 * implementations for managing topic entities. It extends the {@link AbstractService} class, inheriting common
 * service functionalities for basic CRUD operations and validation, and adds functionality specific to topics.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 */
@Service
@Validated
public class TopicServiceImpl extends AbstractService<Topic, TopicDto> implements TopicService {

    /**
     * Injected repository for managing {@link Topic} entities.
     */
    private final TopicRepository topicRepository;

    /**
     * Constructs a new {@code TopicServiceImpl} instance with the given dependencies.
     *
     * @param repository the repository for managing topic entities
     * @param mapper     the mapper for converting between topic entities and DTOs
     */
    public TopicServiceImpl(JpaRepository<Topic, Long> repository, Mapper<Topic, TopicDto> mapper) {
        super(repository, mapper);
        this.topicRepository = (TopicRepository) repository;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ValidationException if the topic name or order is not unique within the course.
     */
    @Override
    public TopicDto save(TopicDto topicDto) {
        List<Topic> courseTopics = topicRepository.findByCourseId(topicDto.getCourseId());

        if (isTopicNameUniqueWithinCourseForSave(topicDto, courseTopics) &&
            isTopicOrderUniqueWithinCourseForSave(topicDto, courseTopics)) {
            return super.save(topicDto);
        }

        throw new ValidationException(HttpStatus.BAD_REQUEST, """
            Error creating new course topic.Topic name and order within
            the course must be unique.""");
    }

    /**
     * {@inheritDoc}
     *
     * @throws ValidationException if the topic name or order is not unique within the course after update.
     */
    @Override
    public TopicDto update(TopicDto topicDto) {
        List<Topic> courseTopics = topicRepository.findByCourseId(topicDto.getCourseId());

        if (isTopicNameUniqueWithinCourseForUpdate(topicDto, courseTopics) &&
            isTopicOrderUniqueWithinCourseForUpdate(topicDto, courseTopics)) {
            return super.update(topicDto);
        }

        throw new ValidationException(HttpStatus.BAD_REQUEST, """
            Error updating course theme. Topic name and order within
            the course must be unique.""");
    }

    private boolean isTopicNameUniqueWithinCourseForSave(TopicDto topic, List<Topic> topics) {
        return topics.stream().noneMatch(t -> t.getTopicName().equals(topic.getTopicName()));
    }

    private boolean isTopicOrderUniqueWithinCourseForSave(TopicDto topic, List<Topic> topics) {
        return topics.stream().noneMatch(t -> t.getTopicOrder().equals(topic.getTopicOrder()));
    }

    private boolean isTopicNameUniqueWithinCourseForUpdate(TopicDto topic, List<Topic> topics) {
        return topics.stream()
            .filter(t -> !t.getId().equals(topic.getId()))
            .noneMatch(t -> t.getTopicName().equals(topic.getTopicName()));
    }

    private boolean isTopicOrderUniqueWithinCourseForUpdate(TopicDto topic, List<Topic> topics) {
        return topics.stream()
            .filter(t -> !t.getId().equals(topic.getId()))
            .noneMatch(t -> t.getTopicOrder().equals(topic.getTopicOrder()));
    }

}
