package ua.foxminded.universitycms.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.model.Course;
import java.util.List;

/**
 * The {@code CourseService} interface defines a set of operations for managing {@link Course} entities
 * and their corresponding {@link CourseDto} representations. It extends the generic {@link Service} interface,
 * providing specialized services for managing courses within the system, including CRUD operations, pagination,
 * searching, fetching student courses, and retrieving author information.
 *
 * @author Serhii Bohdan
 */
public interface CourseService extends Service<Course, CourseDto> {

    /**
     * Retrieves a page of course data containing all courses. This method retrieves a paginated list of all
     * courses from the underlying data store. It utilizes the provided `Pageable` object to specify the
     * page number, size.
     *
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return a Page object containing a list of CourseDto objects representing the requested page of courses
     */
    Page<CourseDto> getAllCoursesInPage(@NotNull Pageable pageable);

    /**
     * Retrieves a page of course data filtered by name. This method retrieves a paginated list of courses
     * whose names contain (case-insensitive) the provided keyword. It utilizes the `Pageable` object to
     * specify the page number, size.
     *
     * @param name     the keyword to filter courses by name (can be blank)
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return a Page object containing a list of CourseDto objects representing the requested page of filtered courses
     */
    Page<CourseDto> getCourseByNameInPage(@NotNull String name, @NotNull Pageable pageable);

    /**
     * Retrieves a list of all course names in the system.
     *
     * @return a list of course names, providing a concise overview of available courses
     */
    List<String> getAllNamesOfCourses();

    /**
     * Retrieves a list of courses enrolled by a student identified by the given student ID.
     *
     * @param studentId the ID of the student to retrieve courses for
     * @return a list of {@link CourseDto} objects representing the student's enrolled courses
     */
    List<CourseDto> getStudentCourses(long studentId);

    /**
     * Retrieves a list of courses enrolled by a student identified by the given student ID,
     * filtered by a specific course name.
     *
     * @param studentId  the ID of the student to retrieve courses for
     * @param courseName the name of the course to filter by
     * @return a list of {@link CourseDto} objects representing the student's enrolled courses matching the provided course name
     */
    List<CourseDto> getStudentCourseByCourseName(long studentId, @NotNull String courseName);

    /**
     * Retrieves a list of courses taught by a teacher identified by the given teacher ID.
     *
     * @param teacherId the ID of the teacher to retrieve courses for
     * @return a list of {@link CourseDto} objects representing the teacher's courses
     */
    List<CourseDto> getTeacherCourses(long teacherId);

    /**
     * Retrieves a list of courses taught by a teacher identified by the given teacher ID,
     * filtered by a specific course name.
     *
     * @param teacherId  the ID of the teacher to retrieve courses for
     * @param courseName the name of the course to filter by
     * @return a list of {@link CourseDto} objects representing the teacher's courses matching the provided course name
     */
    List<CourseDto> getTeacherCourseByCourseName(long teacherId, @NotNull String courseName);

    /**
     * Deducts a student from a specified course.
     *
     * @param courseId  the ID of the course to deduct the student from
     * @param studentId the ID of the student to be deducted
     */
    void deductStudentFromCourse(long courseId, long studentId);

    /**
     * Enrolls a student in a specified course.
     *
     * @param courseId  the ID of the course to enroll the student in
     * @param studentId the ID of the student to be enrolled
     */
    void enrollStudentInCourse(long courseId, long studentId);

}
