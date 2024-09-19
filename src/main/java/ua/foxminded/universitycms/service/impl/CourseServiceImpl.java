package ua.foxminded.universitycms.service.impl;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.service.CourseService;

/**
 * The {@code CourseServiceImpl} class implements the {@link CourseService} interface, providing concrete
 * implementations for managing course entities. It extends the {@link AbstractService} class to inherit common
 * service functionalities and adds course-specific operations.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 * @see CourseRepository
 * @see TeacherRepository
 * @see StudentRepository
 */
@Service
@Validated
@Transactional
public class CourseServiceImpl extends AbstractService<Course, CourseDto> implements CourseService {

    /**
     * Repository for interacting with {@link Course} entities.
     */
    private final CourseRepository courseRepository;

    /**
     * Repository for interacting with {@link Teacher} entities.
     */
    private final TeacherRepository teacherRepository;

    /**
     * Repository for interacting with {@link Student} entities.
     */
    private final StudentRepository studentRepository;

    /**
     * Constructs a new {@code CourseServiceImpl} with the provided repositories and mapper.
     *
     * @param repository        the repository to manage {@link Course} entities
     * @param mapper            the mapper to map between {@link Course} and {@link CourseDto} objects
     * @param teacherRepository the repository to manage {@link Teacher} entities
     * @param studentRepository the repository to manage {@link Student} entities
     */
    public CourseServiceImpl(JpaRepository<Course, Long> repository, Mapper<Course, CourseDto> mapper,
                             TeacherRepository teacherRepository, StudentRepository studentRepository) {
        super(repository, mapper);
        this.courseRepository = (CourseRepository) repository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CourseDto> getAllCoursesInPage(Pageable pageable) {
        return courseRepository.findAll(pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CourseDto> getCourseByNameInPage(String name, Pageable pageable) {
        return courseRepository.findCourseByCourseNameIgnoreCase(name.strip(), pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllNamesOfCourses() {
        return courseRepository.findAll().stream()
            .map(Course::getCourseName)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseDto> getStudentCourses(long studentId) {
        return studentRepository.findById(studentId)
            .map(s -> getCourseDtoList(s.getCourses()))
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, String.format("Student with given ID does not exist: %d", studentId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseDto> getStudentCourseByCourseName(long studentId, String courseName) {
        return getStudentCourses(studentId).stream()
            .filter(c -> c.getCourseName().equals(courseName.strip()))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseDto> getTeacherCourses(long teacherId) {
        return getCourseDtoList(teacherRepository.findById(teacherId).map(Teacher::getCourses)
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, String.format("Teacher with given ID does not exist: %d", teacherId))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseDto> getTeacherCourseByCourseName(long teacherId, String courseName) {
        return getTeacherCourses(teacherId).stream()
            .filter(c -> c.getCourseName().equals(courseName.strip()))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getCoursesNames(Collection<CourseDto> courses) {
        return courses.stream()
            .map(CourseDto::getCourseName)
            .toList();
    }

    private List<CourseDto> getCourseDtoList(Collection<Course> courses) {
        return courses.stream()
            .map(mapper::toDto)
            .toList();
    }

}
