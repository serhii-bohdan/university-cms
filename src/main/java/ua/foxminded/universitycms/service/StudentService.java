package ua.foxminded.universitycms.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.StudentCreationDto;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.Student;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing {@link Student} entities and their DTO representations in the university management
 * system.
 * <p>
 * This interface extends the generic {@link Service} interface, specializing in operations for {@link Student}
 * entities mapped to {@link StudentDto} DTOs. It provides methods for CRUD operations inherited from {@link Service},
 * along with additional functionality such as creating students from creation DTOs, paginated retrieval with email
 * filtering, retrieving unenrolled students, fetching group mappings, updating passwords and full names, and extracting
 * student emails. Implementations of this interface handle business logic related to student management, leveraging
 * validation constraints for data integrity.
 *
 * @author Serhii Bohdan
 * @see Service
 * @see Student
 * @see StudentDto
 * @see StudentCreationDto
 * @see PasswordUpdateRequestDto
 * @see FullName
 */
public interface StudentService extends Service<Student, StudentDto> {

    /**
     * Saves a new student entity based on the provided creation DTO.
     * <p>
     * Creates a new student in the system using the data from the {@link StudentCreationDto}, applying validation
     * rules ({@link Valid}) to ensure data integrity and the {@link NotNull} constraint to ensure the DTO is provided.
     *
     * @param dto the {@link StudentCreationDto} containing the data for the new student, must be non-null and valid
     * @return a {@link StudentDto} representing the saved student, including its generated ID
     */
    StudentDto save(@NotNull @Valid StudentCreationDto dto);

    /**
     * Retrieves a paginated list of students, optionally filtered by email.
     * <p>
     * Fetches students from the system based on the provided {@link Pageable} paging parameters and an optional
     * email filter. The {@link NotNull} constraint ensures that the paging configuration is provided.
     *
     * @param pageable the paging and sorting configuration for the query, must be non-null
     * @param email    an optional email filter; if null or empty, all students are retrieved
     * @return a {@link Page} of {@link StudentDto} objects representing the filtered and paginated students
     */
    Page<StudentDto> findStudents(@NotNull Pageable pageable, String email);

    /**
     * Retrieves a list of students not enrolled in a specific course, optionally filtered by email.
     * <p>
     * Identifies and returns students who are not currently enrolled in the course specified by {@code courseId},
     * with an optional filter by {@code email} to narrow the results.
     *
     * @param courseId the ID of the course to check for unenrolled students
     * @param email    an optional email filter; if null or empty, all unenrolled students are retrieved
     * @return a {@link List} of {@link StudentDto} objects representing students not enrolled in the course
     */
    List<StudentDto> getUnEnrolledStudents(long courseId, String email);

    /**
     * Retrieves a mapping of all existing groups in the system.
     * <p>
     * Returns a map where the keys are group names and the values are their corresponding group IDs, providing
     * a convenient way to access all group information for selection or reference purposes.
     *
     * @return a {@link Map} with group names as keys and their IDs as values
     */
    Map<String, Long> getAllExistingGroups();

    /**
     * Updates a student's password based on the provided request.
     * <p>
     * Modifies the password of a student identified in the {@link PasswordUpdateRequestDto}, ensuring the
     * request meets validation criteria. The {@link NotNull} constraint ensures that the request DTO is provided.
     *
     * @param passwordUpdateRequest the {@link PasswordUpdateRequestDto} containing the password update details,
     *                              must be non-null
     */
    void updateStudentPassword(@NotNull PasswordUpdateRequestDto passwordUpdateRequest);

    /**
     * Extracts email addresses from a collection of students.
     * <p>
     * Converts the provided collection of {@link StudentDto} objects into a list of their email addresses.
     * The {@link NotNull} constraint ensures that the input collection is not null.
     *
     * @param students the collection of {@link StudentDto} objects from which to extract emails, must be non-null
     * @return a {@link List} of email addresses as strings
     */
    List<String> extractStudentEmails(@NotNull Collection<StudentDto> students);

    /**
     * Updates a student's full name.
     * <p>
     * Changes the full name of the student identified by {@code studentId} to the provided {@link FullName} object.
     *
     * @param studentId the ID of the student whose full name is to be updated
     * @param fullName  the new {@link FullName} object containing the updated first and last names
     */
    void updateStudentFullName(long studentId, FullName fullName);

}
