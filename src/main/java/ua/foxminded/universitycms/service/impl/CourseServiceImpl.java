package ua.foxminded.universitycms.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.service.CourseService;

/**
 * The {@code CourseServiceImpl} class implements the {@link CourseService}
 * interface.
 * <p>
 * This class provides the functionality for managing courses.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructs a new {@code CourseServiceImpl} with the specified course
     * repository, teacher repository, and model mapper.
     *
     * @param courseRepository  the course repository
     * @param teacherRepository the teacher repository
     * @param modelMapper       the model mapper
     */
    public CourseServiceImpl(CourseRepository courseRepository, TeacherRepository teacherRepository,
            ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Sets up the model mapper after the bean has been initialized.
     */
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Course.class, CourseDto.class).addMapping(src -> src.getAuthor().getUserId(),
                CourseDto::setAuthorId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addCourse(CourseDto courseDto) {
        boolean isAdded = false;

        if (Objects.nonNull(courseDto) && Objects.nonNull(courseDto.getCourseName())
                && Objects.nonNull(courseDto.getCourseDescription()) && Objects.nonNull(courseDto.getAuthorId())) {
            courseRepository.save(mapToEntity(courseDto));
            isAdded = true;
        }

        return isAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CourseDto> getCourseById(Long courseId) {
        CourseDto findedCourse = null;

        if (Objects.nonNull(courseId)) {
            Optional<Course> optional = courseRepository.findById(courseId);

            if (optional.isPresent()) {
                findedCourse = mapToDto(optional.get());
            }
        }

        return Optional.ofNullable(findedCourse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteCourseById(Long courseId) {
        boolean isDeleted = false;

        if (Objects.nonNull(courseId)) {
            Optional<Course> optional = courseRepository.findById(courseId);

            if (optional.isPresent()) {
                courseRepository.delete(optional.get());
                isDeleted = true;
            }
        }

        return isDeleted;
    }

    private CourseDto mapToDto(Course entity) {
        return modelMapper.map(entity, CourseDto.class);
    }

    private Course mapToEntity(CourseDto dto) {
        Teacher author = teacherRepository.findById(dto.getAuthorId()).get();
        Course course = null;

        if (Objects.nonNull(dto.getCourseId()) && dto.getCourseId() >= 1L) {
            course = courseRepository.findById(dto.getCourseId()).get();
            course.setCourseName(dto.getCourseName());
            course.setCourseDescription(dto.getCourseDescription());
        } else {
            course = modelMapper.map(dto, Course.class);
        }

        course.setAuthor(author);
        return course;
    }

}
