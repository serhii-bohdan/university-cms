package ua.foxminded.universitycms.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Service interface for managing {@link Course} entities and their {@link CourseDto} representations in the university
 * management system.
 * <p>
 * This interface extends the generic {@link Service} interface, specializing in operations specific to courses. It
 * provides methods for CRUD operations inherited from {@link Service}, as well as additional functionality such as
 * paginated course retrieval, student enrollment and deduction, group-based enrollment, course name extraction,
 * user-specific course retrieval, and course filtering by name. Implementations of this interface interact with
 * persistence layers and handle business logic related to course management.
 *
 * @author Serhii Bohdan
 * @see Service
 * @see Course
 * @see CourseDto
 * @see CustomUserDetails
 * @see EntityNotFoundException
 */
public interface CourseService extends Service<Course, CourseDto> {

    /**
     * Retrieves a paginated list of courses, optionally filtered by name.
     * <p>
     * Fetches courses from the system based on the provided {@link Pageable} paging parameters and an optional
     * name filter. The {@link NotNull} constraint ensures that the paging configuration is provided.
     *
     * @param pageable the paging and sorting configuration for the query, must be non-null
     * @param name     an optional name filter; if null or empty, all courses are retrieved
     * @return a {@link Page} of {@link CourseDto} objects representing the filtered and paginated courses
     */
    Page<CourseDto> findCourses(@NotNull Pageable pageable, String name);

    /**
     * Removes a student from a specified course.
     * <p>
     * Deducts the student identified by {@code studentId} from the course identified by {@code courseId}, updating
     * the course's student roster accordingly.
     *
     * @param courseId  the ID of the course from which the student will be removed
     * @param studentId the ID of the student to be deducted from the course
     */
    void deductStudentFromCourse(long courseId, long studentId);

    /**
     * Enrolls a student in a specified course.
     * <p>
     * Adds the student identified by {@code studentId} to the course identified by {@code courseId}, updating
     * the course's student roster to include the new enrollment.
     *
     * @param courseId  the ID of the course in which the student will be enrolled
     * @param studentId the ID of the student to be enrolled in the course
     */
    void enrollStudentInCourse(long courseId, long studentId);

    /**
     * Enrolls all students from a specific group into a course.
     * <p>
     * Retrieves the course by {@code courseId} and all students from the group identified by {@code groupId},
     * then adds those students to the course's student roster. Throws an {@link EntityNotFoundException} if the
     * course is not found.
     *
     * @param courseId the ID of the course in which to enroll students
     * @param groupId  the ID of the group whose students will be enrolled
     * @throws EntityNotFoundException if the course with the specified ID does not exist
     */
    void enrollAllStudentsFromGroupInCourse(long courseId, long groupId);

    /**
     * Extracts course names from a collection of courses.
     * <p>
     * Converts the provided collection of {@link CourseDto} objects into a list of their names. The {@link NotNull}
     * constraint ensures that the input collection is not null.
     *
     * @param courses the collection of {@link CourseDto} objects from which to extract names, must be non-null
     * @return a {@link List} of course names as strings
     */
    List<String> extractCourseNames(@NotNull Collection<CourseDto> courses);

    /**
     * Retrieves the courses associated with a specific user.
     * <p>
     * Fetches a list of courses linked to the user represented by {@link CustomUserDetails}, such as courses taught by
     * a teacher or enrolled in by a student. The {@link NotNull} constraint ensures that the user details are provided.
     *
     * @param customUserDetails the {@link CustomUserDetails} object representing the user, must be non-null
     * @return a {@link List} of {@link CourseDto} objects representing the user's courses
     */
    List<CourseDto> getUserCourses(@NotNull CustomUserDetails customUserDetails);

    /**
     * Filters a list of courses by name.
     * <p>
     * Returns a subset of the provided {@link CourseDto} list where the course names match or contain the specified
     * {@code courseName}. The {@link NotNull} constraint ensures that the input list is not null. If {@code courseName}
     * is null or empty, the original list may be returned unfiltered, depending on the implementation.
     *
     * @param courses    the list of {@link CourseDto} objects to filter, must be non-null
     * @param courseName the name or partial name to filter courses by; may be null or empty
     * @return a {@link List} of {@link CourseDto} objects matching the name filter
     */
    List<CourseDto> filterCoursesByName(@NotNull List<CourseDto> courses, String courseName);

    /**
     * Filters a set of course students by matching their email to the provided value.
     * <p>
     * Returns the original set if the email is blank; otherwise, returns a new set containing only
     * the student whose email exactly matches the provided value. If no match is found, an empty set
     * is returned.
     *
     * @param courseStudents the set of {@link StudentDto} objects representing course students
     * @param email          the email address to match against student records
     * @return a {@link Set} of {@link StudentDto} objects containing the matching student, or an empty set
     */
    Set<StudentDto> filterCourseStudentsByEmail(Set<StudentDto> courseStudents, String email);

}
