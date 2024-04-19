package ua.foxminded.universitycms.service.impl;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.Mapper;
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
@Transactional
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
     * Retrieves a list of marks for a student in a given course.
     *
     * @param studentId the ID of the student
     * @param courseId  the ID of the course
     * @return a list of {@link MarkDto} objects representing the student's marks in the course
     * @throws ServiceException if an error occurs during retrieval
     */
    @Override
    public List<MarkDto> getStudentCourseMarks(long studentId, long courseId) {
        return getMarkDtoList(markRepository.findMarksByStudentIdAndCourseId(studentId, courseId));
    }

    /**
     * Retrieves a list of marks for a student in a given course and topic.
     *
     * @param studentId the ID of the student
     * @param courseId  the ID of the course
     * @param topicName the name of the topic
     * @return a list of {@link MarkDto} objects representing the student's marks for the specified topic
     */
    @Override
    public List<MarkDto> getStudentCourseMarksByTopicName(long studentId, long courseId, String topicName) {
        return markRepository.findMarksByStudentIdAndCourseId(studentId, courseId).stream()
            .filter(m -> m.getTopic().getTopicName().equals(topicName.strip()))
            .map(mapper::toDto)
            .toList();
    }

    /**
     * Retrieves the names of topics within a given course.
     *
     * @param courseId the ID of the course
     * @return a list of topic names as strings
     * @throws ServiceException if an error occurs during retrieval
     */
    @Override
    public List<String> getNamesOfTopicsInCourse(long courseId) {
        return topicRepository.findByCourseId(courseId).stream()
            .map(Topic::getTopicName)
            .toList();
    }

    private List<MarkDto> getMarkDtoList(List<Mark> marks) {
        return marks.stream()
            .map(mapper::toDto)
            .toList();
    }

}
