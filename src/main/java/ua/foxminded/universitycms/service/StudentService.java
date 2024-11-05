package ua.foxminded.universitycms.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.model.Student;
import java.util.List;
import java.util.Map;

/**
 * The {@code StudentService} interface defines a set of operations for managing {@link Student} entities and their
 * corresponding {@link StudentDto} representations. It extends the generic {@link Service} interface, offering
 * specialized services specifically tailored for students, including paginated retrieval, searching by name, and
 * group-based operations.
 *
 * @author Serhii Bohdan
 */
public interface StudentService extends Service<Student, StudentDto> {

    /**
     * Creates and saves a new student along with their associated schedule.
     * <p>
     * This method takes a {@link StudentDto} object and a plain-text `password`, encodes the password using the
     * {@link PasswordEncoder}. The provided `dto` must be valid according to its validation constraints, and the
     * `password` must not be blank.
     *
     * @param dto      the {@link StudentDto} object representing the new student
     * @param password the plain-text password for the new student
     * @return the saved {@link StudentDto} object, with the password hashed
     */
    StudentDto save(@NotNull @Valid StudentDto dto, @NotBlank String password);

    /**
     * Retrieves a page of student data containing all students. This method retrieves
     * a paginated list of all students from the underlying data store. It utilizes the provided
     * `Pageable` object to specify the page number, size.
     *
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return a Page object containing a list of StudentDto objects representing the requested page of students
     */
    Page<StudentDto> getStudentsPage(@NotNull Pageable pageable);

    /**
     * Retrieves a paginated list of students by their email address.
     * <p>
     * This method queries students based on the specified email address, returning
     * results as a {@link Page} of {@link StudentDto} objects.
     *
     * @param email    the email address of the students to search for
     * @param pageable the pagination information, including page number and size
     * @return a {@link Page} containing {@link StudentDto} objects matching the specified email
     */
    Page<StudentDto> getStudentInPageByEmail(@NotNull String email, @NotNull Pageable pageable);

    /**
     * Retrieves a list of students who are not enrolled in the specified course.
     *
     * @param courseId the ID of the course
     * @return a list of {@link StudentDto} objects representing the students who are not enrolled in the course
     */
    List<StudentDto> getListOfStudentsNotEnrolledInCourse(long courseId);

    /**
     * Retrieves a map of all existing groups.
     * <p>
     * The map contains group names as keys and group IDs as values, allowing
     * quick access to all existing group information.
     *
     * @return a map with group names as keys and corresponding group IDs as values
     */
    Map<String, Long> getAllExistingGroups();

    /**
     * Updates the password of an existing student.
     * <p>
     * This method takes a {@link PasswordUpdateRequestDto} object containing the student ID, current password,
     * new password, and password confirmation. It validates the request and, if successful, updates the student's
     * password in the system.
     *
     * @param passwordUpdateRequest the DTO containing the password update information
     */
    void updateStudentPassword(@NotNull PasswordUpdateRequestDto passwordUpdateRequest);

}
