package ua.foxminded.universitycms.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Lesson;
import ua.foxminded.universitycms.model.StudyDay;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.LessonRepository;
import ua.foxminded.universitycms.repository.StudyDayRepository;
import ua.foxminded.universitycms.service.LessonService;

/**
 * The {@code LessonServiceImpl} class implements the {@link LessonService}
 * interface.
 * <p>
 * This class provides the functionality for managing lessons.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final StudyDayRepository studyDayRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructs a new {@code LessonServiceImpl} with the specified lesson
     * repository, study day repository, course repository, and model mapper.
     *
     * @param lessonRepository   the lesson repository
     * @param studyDayRepository the study day repository
     * @param courseRepository   the course repository
     * @param modelMapper        the model mapper
     */
    public LessonServiceImpl(LessonRepository lessonRepository, StudyDayRepository studyDayRepository,
            CourseRepository courseRepository, ModelMapper modelMapper) {
        this.lessonRepository = lessonRepository;
        this.studyDayRepository = studyDayRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Sets up the model mapper after the bean has been initialized.
     */
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Lesson.class, LessonDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getStudyDay().getStudyDayId(), LessonDto::setStudyDayId);
            mapper.map(src -> src.getCourse().getCourseId(), LessonDto::setCourseId);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addLesson(LessonDto lessonDto) {
        boolean isAdded = false;

        if (Objects.nonNull(lessonDto) && Objects.nonNull(lessonDto.getLessonStartTime())
                && Objects.nonNull(lessonDto.getLessonEndTime()) && Objects.nonNull(lessonDto.getStudyDayId())
                && Objects.nonNull(lessonDto.getCourseId())) {
            lessonRepository.save(mapToEntity(lessonDto));
            isAdded = true;
        }

        return isAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<LessonDto> getLessonById(Long lessonId) {
        LessonDto findedLesson = null;

        if (Objects.nonNull(lessonId)) {
            Optional<Lesson> optional = lessonRepository.findById(lessonId);

            if (optional.isPresent()) {
                findedLesson = mapToDto(optional.get());
            }
        }

        return Optional.ofNullable(findedLesson);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteLessonById(Long lessonId) {
        boolean isDeleted = false;

        if (Objects.nonNull(lessonId)) {
            Optional<Lesson> optional = lessonRepository.findById(lessonId);

            if (optional.isPresent()) {
                lessonRepository.delete(optional.get());
                isDeleted = true;
            }
        }

        return isDeleted;
    }

    private LessonDto mapToDto(Lesson entity) {
        return modelMapper.map(entity, LessonDto.class);
    }

    private Lesson mapToEntity(LessonDto dto) {
        StudyDay studyDay = studyDayRepository.findById(dto.getStudyDayId()).get();
        Course course = courseRepository.findById(dto.getCourseId()).get();
        Lesson lesson = null;

        if (Objects.nonNull(dto.getLessonId()) && dto.getLessonId() >= 1L) {
            lesson = lessonRepository.findById(dto.getLessonId()).get();
            lesson.setLessonStartTime(dto.getLessonStartTime());
            lesson.setLessonEndTime(dto.getLessonEndTime());
        } else {
            lesson = modelMapper.map(dto, Lesson.class);
        }

        lesson.setStudyDay(studyDay);
        lesson.setCourse(course);
        return lesson;
    }

}
