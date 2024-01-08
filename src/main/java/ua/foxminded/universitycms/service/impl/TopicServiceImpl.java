package ua.foxminded.universitycms.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.dto.TopicDto;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Topic;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.TopicRepository;
import ua.foxminded.universitycms.service.TopicService;

/**
 * The {@code TopicServiceImpl} class implements the {@link TopicService}
 * interface.
 * <p>
 * This class provides the functionality for managing topics.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructs a new {@code TopicServiceImpl} with the specified topic
     * repository, course repository, and model mapper.
     *
     * @param topicRepository  the topic repository
     * @param courseRepository the course repository
     * @param modelMapper      the model mapper
     */
    public TopicServiceImpl(TopicRepository topicRepository, CourseRepository courseRepository,
            ModelMapper modelMapper) {
        this.topicRepository = topicRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Sets up the model mapper after the bean has been initialized.
     */
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Topic.class, TopicDto.class).addMapping(src -> src.getCourse().getCourseId(),
                TopicDto::setCourseId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addTopic(TopicDto topicDto) {
        boolean isAdded = false;

        if (Objects.nonNull(topicDto) && Objects.nonNull(topicDto.getTopicName())
                && Objects.nonNull(topicDto.getTopicDescription()) && Objects.nonNull(topicDto.getCourseId())) {
            topicRepository.save(mapToEntity(topicDto));
            isAdded = true;
        }

        return isAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TopicDto> getTopicById(Long topicId) {
        TopicDto findedTopic = null;

        if (Objects.nonNull(topicId)) {
            Optional<Topic> optional = topicRepository.findById(topicId);

            if (optional.isPresent()) {
                findedTopic = mapToDto(optional.get());
            }
        }

        return Optional.ofNullable(findedTopic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteTopicById(Long topicId) {
        boolean isDeleted = false;

        if (Objects.nonNull(topicId)) {
            Optional<Topic> optional = topicRepository.findById(topicId);

            if (optional.isPresent()) {
                topicRepository.delete(optional.get());
                isDeleted = true;
            }
        }

        return isDeleted;
    }

    private TopicDto mapToDto(Topic entity) {
        return modelMapper.map(entity, TopicDto.class);
    }

    private Topic mapToEntity(TopicDto dto) {
        Course course = courseRepository.findById(dto.getCourseId()).get();
        Topic topic = null;

        if (Objects.nonNull(dto.getTopicId()) && dto.getTopicId() >= 1L) {
            topic = topicRepository.findById(dto.getTopicId()).get();
            topic.setTopicName(dto.getTopicName());
            topic.setTopicDescription(dto.getTopicDescription());
        } else {
            topic = modelMapper.map(dto, Topic.class);
        }

        topic.setCourse(course);
        return topic;
    }

}
