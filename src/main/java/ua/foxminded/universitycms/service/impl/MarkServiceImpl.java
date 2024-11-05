package ua.foxminded.universitycms.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.AbstractEntity;
import ua.foxminded.universitycms.model.Mark;
import ua.foxminded.universitycms.model.Topic;
import ua.foxminded.universitycms.repository.MarkRepository;
import ua.foxminded.universitycms.repository.TopicRepository;
import ua.foxminded.universitycms.service.MarkService;

/**
 * The {@code MarkServiceImpl} class implements the {@link MarkService} interface, providing concrete
 * implementations for managing mark entities. It extends the {@link AbstractService} class, inheriting common
 * service functionalities for basic CRUD operations and validation, and adds functionality specific to marks.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 * @see MarkRepository
 * @see TopicRepository
 */
@Service
@Validated
public class MarkServiceImpl extends AbstractService<Mark, MarkDto> implements MarkService {

    /**
     * The repository for managing {@link Mark} entities.
     */
    private final MarkRepository markRepository;

    /**
     * The repository for managing {@link Topic} entities.
     */
    private final TopicRepository topicRepository;

    /**
     * Constructs a new {@code MarkServiceImpl} instance with the given dependencies.
     *
     * @param repository      the repository for managing mark entities
     * @param mapper          the mapper for converting between mark entities and DTOs
     * @param topicRepository the repository for managing topic entities
     */
    public MarkServiceImpl(JpaRepository<Mark, Long> repository, Mapper<Mark, MarkDto> mapper, TopicRepository topicRepository) {
        super(repository, mapper);
        this.markRepository = (MarkRepository) repository;
        this.topicRepository = topicRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MarkDto> getStudentCourseMarks(long studentId, long courseId) {
        return getMarkDtoList(markRepository.findMarksByStudentIdAndCourseId(studentId, courseId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MarkDto> getStudentCourseMarksByTopicName(long studentId, long courseId, String topicName) {
        return getMarkDtoList(markRepository.findMarksByStudentIdAndCourseId(studentId, courseId).stream()
            .filter(m -> m.getTopic().getTopicName().equals(topicName.strip()))
            .toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getNamesOfTopicsInCourse(long courseId) {
        return topicRepository.findByCourseId(courseId).stream()
            .map(Topic::getTopicName)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getUnratedTopics(long studentId, long courseId) {
        List<Long> ratedTopicIds = getStudentCourseMarks(studentId, courseId).stream()
            .map(MarkDto::getTopicId)
            .toList();

        return topicRepository.findByCourseId(courseId).stream()
            .filter(t -> !ratedTopicIds.contains(t.getId()))
            .collect(Collectors.toMap(Topic::getTopicName, AbstractEntity::getId));
    }

    private List<MarkDto> getMarkDtoList(List<Mark> marks) {
        return marks.stream()
            .map(mapper::toDto)
            .toList();
    }

}
