package ua.foxminded.universitycms.service.impl;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.exception.ServiceException;
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
     * Retrieves a page of all courses from the database using pagination.
     *
     * @param pageable the pagination information specifying the page number, size
     * @return a page of course DTOs representing the requested page of courses with pagination information
     */
    @Override
    public Page<CourseDto> getAllCoursesInPage(Pageable pageable) {
        return courseRepository.findAll(pageable).map(mapper::toDto);
    }

    /**
     * Retrieves a page of courses filtered by name using pagination.
     *
     * @param name     the name of the course to filter by
     * @param pageable the pagination information specifying the page number, size
     * @return a page of course DTOs representing the requested page of filtered courses with pagination information
     */
    @Override
    public Page<CourseDto> getCourseByNameInPage(String name, Pageable pageable) {
        return courseRepository.findCourseByCourseNameIgnoreCase(name.strip(), pageable).map(mapper::toDto);
    }

    /**
     * Retrieves a list of all course names in the system.
     *
     * @return a list of course names, providing a concise overview of available courses
     */
    @Override
    public List<String> getAllNamesOfCourses() {
        return courseRepository.findAll().stream()
            .map(Course::getCourseName)
            .toList();
    }

    /**
     * Retrieves a list of courses enrolled by a student identified by the given student ID.
     *
     * @param studentId the ID of the student to retrieve courses for
     * @return a list of {@link CourseDto} objects representing the student's enrolled courses
     * @throws ServiceException if a student with the provided ID is not found.
     */
    @Override
    public List<CourseDto> getStudentCourses(long studentId) {
        return studentRepository.findById(studentId)
            .map(s -> getCourseDtoList(s.getCourses()))
            .orElseThrow(() -> new ServiceException("User with given ID does not exist: " + studentId));
    }

    /**
     * Retrieves a list of courses enrolled by a student identified by the given student ID,
     * filtered by a specific course name.
     *
     * @param studentId  the ID of the student to retrieve courses for
     * @param courseName the name of the course to filter by
     * @return a list of {@link CourseDto} objects representing the student's enrolled courses matching the provided course name
     * @throws ServiceException if a student with the provided ID is not found.
     */
    @Override
    public List<CourseDto> getStudentCourseByCourseName(long studentId, String courseName) {
        return getStudentCourses(studentId).stream()
            .filter(courseDto -> courseDto.getCourseName().equals(courseName.strip()))
            .toList();
    }

    /**
     * Retrieves a list of names of courses enrolled by a student identified by the given student ID.
     *
     * @param studentId the ID of the student to retrieve course names for
     * @return a list of course names representing the student's enrolled courses
     * @throws ServiceException if a student with the provided ID is not found.
     */
    @Override
    public List<String> getStudentCoursesNames(long studentId) {
        return getStudentCourses(studentId).stream()
            .map(CourseDto::getCourseName)
            .toList();
    }

    /**
     * Retrieves the full name of the teacher associated with a given teacher ID.
     *
     * @param teacherId the ID of the teacher to retrieve the full name for
     * @return the full name of the teacher in the format "FirstName LastName"
     * @throws ServiceException if no teacher exists with the provided ID
     */
    @Override
    public String getAuthorFullNameByTeacherId(long teacherId) {
        return teacherRepository.findById(teacherId).map(t -> t.getName().getFirstName() + " " + t.getName().getLastName())
            .orElseThrow(() -> new ServiceException("Teacher with given ID does not exist: " + teacherId));
    }

    private List<CourseDto> getCourseDtoList(Collection<Course> courses) {
        return courses.stream()
            .map(mapper::toDto)
            .toList();
    }

}
