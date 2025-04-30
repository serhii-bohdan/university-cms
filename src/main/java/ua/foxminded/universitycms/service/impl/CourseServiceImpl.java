package ua.foxminded.universitycms.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.InvalidUserRoleException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.exception.ValidationException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.CourseService;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link CourseService} interface for managing {@link Course} entities in the university
 * management system.
 * <p>
 * This service class extends {@link AbstractService} to leverage common CRUD operations and provides concrete
 * implementations for course-specific operations such as paginated retrieval, student enrollment/deduction, group
 * enrollment, and user course retrieval. It uses {@link CourseRepository}, {@link TeacherRepository}, and
 * {@link StudentRepository} for data access, and a {@link Mapper} for entity-DTO conversions. The {@code @Service}
 * annotation marks it as a Spring-managed bean, and {@code @Validated} enables validation.
 *
 * @author Serhii Bohdan
 * @see CourseService
 * @see AbstractService
 * @see CourseRepository
 * @see TeacherRepository
 * @see StudentRepository
 * @see Mapper
 * @see EntityNotFoundException
 * @see ValidationException
 * @see InvalidUserRoleException
 * @see UserNotFoundException
 */
@Service
@Validated
public class CourseServiceImpl extends AbstractService<Course, CourseDto> implements CourseService {

    /**
     * Error message template used when a course with the specified ID cannot be found.
     */
    private static final String COURSE_NOT_FOUND_MESSAGE = "Course not found with ID: %s.";

    /**
     * Error message template used when a student with the specified ID cannot be found.
     */
    private static final String STUDENT_NOT_FOUND_MESSAGE = "Student not found with ID: %s.";

    /**
     * Error message used when a student is not enrolled in a course during a deduction attempt.
     */
    private static final String STUDENT_NOT_ENROLLED_IN_COURSE_MESSAGE = "Student is not enrolled in this course.";

    /**
     * Error message used when a student is already enrolled in a course during an enrollment attempt.
     */
    private static final String STUDENT_ALREADY_ENROLLED_IN_COURSE_MESSAGE = "Student already enrolled in this course.";

    /**
     * Error message template used when a teacher with the specified ID cannot be found.
     */
    private static final String TEACHER_NOT_FOUND_MESSAGE = "Teacher with given ID does not exist: %s.";

    /**
     * Error message used when a user lacks permission to access course data.
     * <p>
     * This message is included in an {@link InvalidUserRoleException} when an unauthorized role attempts to retrieve courses.
     */
    private static final String COURSE_ACCESS_DENIED_MESSAGE = """
        An error occurred while trying to get a list of courses. You do not have permission to read courses.
        """;

    /**
     * Repository for performing CRUD operations on {@link Course} entities.
     * <p>
     * This {@link CourseRepository} instance provides data access methods specific to courses, extending {@link JpaRepository}.
     */
    private final CourseRepository courseRepository;

    /**
     * Repository for accessing {@link Teacher} entities.
     * <p>
     * Used to retrieve teacher-specific course data, such as courses taught by a teacher.
     */
    private final TeacherRepository teacherRepository;

    /**
     * Repository for accessing {@link Student} entities.
     * <p>
     * Used for operations involving student enrollment and deduction in courses.
     */
    private final StudentRepository studentRepository;

    /**
     * Constructs a new {@code CourseServiceImpl} with the required dependencies.
     * <p>
     * Initializes the parent {@link AbstractService} with the provided repository and mapper, and sets up specific
     * repositories for course, teacher, and student management.
     *
     * @param repository        the {@link JpaRepository} for {@link Course} entities, providing basic CRUD operations
     * @param mapper            the {@link Mapper} instance for converting between {@link Course} and {@link CourseDto}
     *                          objects
     * @param teacherRepository the {@link TeacherRepository} for managing teacher entities
     * @param studentRepository the {@link StudentRepository} for managing student entities
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
    public Page<CourseDto> findCourses(Pageable pageable, String name) {
        return StringUtils.isBlank(name)
            ? courseRepository.findAll(pageable).map(mapper::toDto)
            : courseRepository.findCourseByCourseNameIgnoreCase(name, pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deductStudentFromCourse(long courseId, long studentId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                COURSE_NOT_FOUND_MESSAGE.formatted(courseId)
            ));

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                STUDENT_NOT_FOUND_MESSAGE.formatted(studentId)
            ));

        if (!course.getStudents().contains(student)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                STUDENT_NOT_ENROLLED_IN_COURSE_MESSAGE);
        }

        course.removeStudent(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void enrollStudentInCourse(long courseId, long studentId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                COURSE_NOT_FOUND_MESSAGE.formatted(courseId)
            ));

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                STUDENT_NOT_FOUND_MESSAGE.formatted(studentId)
            ));

        if (course.getStudents().contains(student)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                STUDENT_ALREADY_ENROLLED_IN_COURSE_MESSAGE);
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
                COURSE_NOT_FOUND_MESSAGE.formatted(courseId)
            ));

        List<Student> groupStudents = studentRepository.findByGroupId(groupId);

        for (Student student : groupStudents) {
            course.addStudent(student);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> extractCourseNames(Collection<CourseDto> courses) {
        return courses.stream()
            .map(CourseDto::getCourseName)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getUserCourses(CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getId();
        RoleName userRole = customUserDetails.getRoleName();

        Collection<Course> userCourses = switch (userRole) {
            case TEACHER -> teacherRepository.findById(userId).map(Teacher::getCourses)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                    TEACHER_NOT_FOUND_MESSAGE.formatted(userId)));
            case STUDENT -> courseRepository.findStudentCoursesByStudentId(userId);
            default -> throw new InvalidUserRoleException(HttpStatus.FORBIDDEN, COURSE_ACCESS_DENIED_MESSAGE);
        };

        return mapper.toDtoList(userCourses);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseDto> filterCoursesByName(List<CourseDto> courses, String courseName) {
        return StringUtils.isBlank(courseName)
            ? courses
            : courses.stream()
            .filter(course -> course.getCourseName().equals(courseName))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<StudentDto> filterCourseStudentsByEmail(Set<StudentDto> courseStudents, String email) {
        return StringUtils.isBlank(email)
            ? courseStudents
            : courseStudents.stream()
            .filter(s -> s.getEmail().equals(email))
            .collect(Collectors.toSet());
    }

}
