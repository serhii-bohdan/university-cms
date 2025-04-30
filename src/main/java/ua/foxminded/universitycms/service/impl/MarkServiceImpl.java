package ua.foxminded.universitycms.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
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
 * Implementation of the {@link MarkService} interface for managing {@link Mark} entities in the university management
 * system.
 * <p>
 * This service class extends {@link AbstractService} to leverage common CRUD operations and provides concrete
 * implementations for mark-specific operations such as retrieving marks by student and course, fetching topic names,
 * and identifying unrated topics. It uses {@link MarkRepository} and {@link TopicRepository} for data access and a
 * {@link Mapper} for entity-DTO conversions. The {@code @Service} annotation marks it as a Spring-managed bean, and
 * {@code @Validated} enables validation.
 *
 * @author Serhii Bohdan
 * @see MarkService
 * @see AbstractService
 * @see MarkRepository
 * @see TopicRepository
 * @see Mapper
 */
@Service
@Validated
public class MarkServiceImpl extends AbstractService<Mark, MarkDto> implements MarkService {

    /**
     * Repository for performing CRUD operations on {@link Mark} entities.
     * <p>
     * This {@link MarkRepository} instance provides data access methods specific to marks,
     * extending {@link JpaRepository}.
     */
    private final MarkRepository markRepository;

    /**
     * Repository for accessing {@link Topic} entities.
     * <p>
     * Used to retrieve topics associated with courses for mark-related operations.
     */
    private final TopicRepository topicRepository;

    /**
     * Constructs a new {@code MarkServiceImpl} instance with the required dependencies.
     * <p>
     * Initializes the parent {@link AbstractService} with the provided repository and mapper, and sets up specific
     * repositories for mark and topic management.
     *
     * @param repository      the {@link JpaRepository} for {@link Mark} entities, providing basic CRUD operations
     * @param mapper          the {@link Mapper} instance for converting between {@link Mark} and {@link MarkDto} objects
     * @param topicRepository the {@link TopicRepository} for managing topic entities
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
    public List<MarkDto> findStudentCourseMarksByTopicName(long studentId, long courseId, String topicName) {
        List<Mark> studentCourseMarks = markRepository.findMarksByStudentIdAndCourseId(studentId, courseId);
        List<Mark> filteredMarks = filterMarksByTopic(studentCourseMarks, topicName);
        return mapper.toDtoList(filteredMarks);
    }

    private List<Mark> filterMarksByTopic(List<Mark> marks, String topicName) {
        return StringUtils.isBlank(topicName)
            ? marks
            : marks.stream()
            .filter(mark -> topicName.equals(mark.getTopic().getTopicName()))
            .toList();
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
        List<Long> ratedTopicIds = markRepository.findMarksByStudentIdAndCourseId(studentId, courseId).stream()
            .map(mark -> mark.getTopic().getId())
            .toList();

        return topicRepository.findByCourseId(courseId).stream()
            .filter(t -> !ratedTopicIds.contains(t.getId()))
            .collect(Collectors.toMap(Topic::getTopicName, AbstractEntity::getId));
    }

}
