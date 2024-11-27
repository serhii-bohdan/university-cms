package ua.foxminded.universitycms.service.impl;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.exception.ValidationException;
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
public class CourseServiceImpl extends AbstractService<Course, CourseDto> implements CourseService {

    /**
     * The error message used when a course with a specified ID is not found.
     */
    private static final String COURSE_NOT_FOUND_MESSAGE = "Course with ID %d not found";

    /**
     * The error message used when a student with a specified ID is not found.
     */
    private static final String STUDENT_NOT_FOUND_MESSAGE = "Student with ID %d not found";

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
    @Transactional(readOnly = true)
    public Page<CourseDto> getAllCoursesInPage(Pageable pageable) {
        return courseRepository.findAll(pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseDto> getCourseByNameInPage(String name, Pageable pageable) {
        return courseRepository.findCourseByCourseNameIgnoreCase(name.strip(), pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getStudentCourses(long studentId) {
        return studentRepository.findById(studentId)
            .map(s -> getCourseDtoList(s.getCourses()))
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                String.format("Student with given ID does not exist: %d", studentId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getStudentCourseByCourseName(long studentId, String courseName) {
        return getStudentCourses(studentId).stream()
            .filter(c -> c.getCourseName().equals(courseName.strip()))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getTeacherCourses(long teacherId) {
        return getCourseDtoList(teacherRepository.findById(teacherId).map(Teacher::getCourses)
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                String.format("Teacher with given ID does not exist: %d", teacherId))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getTeacherCourseByCourseName(long teacherId, String courseName) {
        return getTeacherCourses(teacherId).stream()
            .filter(c -> c.getCourseName().equals(courseName.strip()))
            .toList();
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if the course or student is not found
     * @throws ValidationException     if the student is not enrolled in the course
     */
    @Override
    @Transactional
    public void deductStudentFromCourse(long courseId, long studentId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                String.format(COURSE_NOT_FOUND_MESSAGE, courseId)
            ));

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                String.format(STUDENT_NOT_FOUND_MESSAGE, studentId)
            ));

        if (!course.getStudents().contains(student)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                "Student is not enrolled in this course");
        }

        course.removeStudent(student);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if the course or student is not found
     * @throws ValidationException     if the student is not enrolled in the course
     */
    @Override
    @Transactional
    public void enrollStudentInCourse(long courseId, long studentId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                String.format(COURSE_NOT_FOUND_MESSAGE, courseId)
            ));

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                String.format(STUDENT_NOT_FOUND_MESSAGE, studentId)
            ));

        if (course.getStudents().contains(student)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                "Student already enrolled in this course");
        }

        course.addStudent(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void enrollAllStudentsFromGroupInCourse(long courseId, long groupId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                String.format(COURSE_NOT_FOUND_MESSAGE, courseId)
            ));

        List<Student> groupStudents = studentRepository.findByGroupId(groupId);

        for (Student student : groupStudents) {
            course.addStudent(student);
        }
    }

    private List<CourseDto> getCourseDtoList(Collection<Course> courses) {
        return courses.stream()
            .map(mapper::toDto)
            .toList();
    }

}
