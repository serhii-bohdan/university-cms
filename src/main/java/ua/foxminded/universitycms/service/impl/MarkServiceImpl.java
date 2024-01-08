package ua.foxminded.universitycms.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.model.Mark;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Topic;
import ua.foxminded.universitycms.repository.MarkRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.repository.TopicRepository;
import ua.foxminded.universitycms.service.MarkService;

/**
 * The {@code MarkServiceImpl} class implements the {@link MarkService}
 * interface.
 * <p>
 * This class provides the functionality for managing marks.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class MarkServiceImpl implements MarkService {

    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;
    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructs a new {@code MarkServiceImpl} with the specified mark repository,
     * student repository, topic repository, and model mapper.
     *
     * @param markRepository    the mark repository
     * @param studentRepository the student repository
     * @param topicRepository   the topic repository
     * @param modelMapper       the model mapper
     */
    public MarkServiceImpl(MarkRepository markRepository, StudentRepository studentRepository,
            TopicRepository topicRepository, ModelMapper modelMapper) {
        this.markRepository = markRepository;
        this.studentRepository = studentRepository;
        this.topicRepository = topicRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Sets up the model mapper after the bean has been initialized.
     */
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Mark.class, MarkDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getStudent().getUserId(), MarkDto::setStudentId);
            mapper.map(src -> src.getTopic().getTopicId(), MarkDto::setTopicId);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addMark(MarkDto markDto) {
        boolean isAdded = false;

        if (Objects.nonNull(markDto) && Objects.nonNull(markDto.getMarkValue())
                && Objects.nonNull(markDto.getStudentId()) && Objects.nonNull(markDto.getTopicId())) {
            markRepository.save(mapToEntity(markDto));
            isAdded = true;
        }

        return isAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MarkDto> getMarkById(Long markId) {
        MarkDto findedMark = null;

        if (Objects.nonNull(markId)) {
            Optional<Mark> optional = markRepository.findById(markId);

            if (optional.isPresent()) {
                findedMark = mapToDto(optional.get());
            }
        }

        return Optional.ofNullable(findedMark);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteMarkById(Long markId) {
        boolean isDeleted = false;

        if (Objects.nonNull(markId)) {
            Optional<Mark> optional = markRepository.findById(markId);

            if (optional.isPresent()) {
                markRepository.delete(optional.get());
                isDeleted = true;
            }
        }

        return isDeleted;
    }

    private MarkDto mapToDto(Mark entity) {
        return modelMapper.map(entity, MarkDto.class);
    }

    private Mark mapToEntity(MarkDto dto) {
        Student student = studentRepository.findById(dto.getStudentId()).get();
        Topic topic = topicRepository.findById(dto.getTopicId()).get();
        Mark mark = null;

        if (Objects.nonNull(dto.getMarkId()) && dto.getMarkId() >= 1L) {
            mark = markRepository.findById(dto.getMarkId()).get();
            mark.setMarkValue(dto.getMarkValue());
        } else {
            mark = modelMapper.map(dto, Mark.class);
        }

        mark.setStudent(student);
        mark.setTopic(topic);
        return mark;
    }

}
