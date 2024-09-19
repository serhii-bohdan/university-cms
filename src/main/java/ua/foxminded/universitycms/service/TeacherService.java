package ua.foxminded.universitycms.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.model.Teacher;
import java.util.List;

/**
 * The {@code TeacherService} interface defines a set of operations for managing {@link Teacher} entities and their
 * corresponding {@link TeacherDto} representations. It extends the generic {@link Service} interface, offering
 * specialized services specifically tailored for teachers, including paginated retrieval, searching by name, and
 * schedule management.
 *
 * @author Serhii Bohdan
 */
public interface TeacherService extends Service<Teacher, TeacherDto> {

    /**
     * Creates and saves a new teacher along with their associated schedule.
     * <p>
     * This method takes a {@link TeacherDto} object and a plain-text `password`, encodes the password using the
     * {@link PasswordEncoder}. The provided `dto` must be valid according to its validation constraints, and the
     * `password` must not be blank.
     *
     * @param dto      the {@link TeacherDto} object representing the new teacher
     * @param password the plain-text password for the new teacher
     * @return the saved {@link TeacherDto} object, with the password hashed
     */
    TeacherDto save(@NotNull @Valid TeacherDto dto, @NotBlank String password);

    /**
     * Retrieves a page of teacher data containing all teachers. This method retrieves a paginated list
     * of all teachers from the underlying data store. It utilizes the provided `Pageable` object to specify
     * the page number, size.
     *
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return a Page object containing a list of TeacherDto objects representing the requested page of teachers
     */
    Page<TeacherDto> getTeachersPage(@NotNull Pageable pageable);

    /**
     * Retrieves a page of teacher data filtered by full name. This method retrieves a paginated list of teachers
     * whose full names contain (case-insensitive) the provided keyword. It utilizes the `Pageable` object to specify
     * the page number, size.
     *
     * @param fullName the keyword to filter teachers by full name (can be blank)
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return a Page object containing a list of TeacherDto objects representing the requested page of filtered teachers
     */
    Page<TeacherDto> getTeacherInPageByName(@NotNull String fullName, @NotNull Pageable pageable);

    /**
     * Retrieves a list of all teacher names in the system.
     *
     * @return a list of teacher names
     */
    List<String> getAllNamesOfTeachers();

}
