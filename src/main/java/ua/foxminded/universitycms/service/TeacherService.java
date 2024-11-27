package ua.foxminded.universitycms.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.TeacherCreationDto;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.model.Teacher;

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
     * Saves a new teacher entity based on the provided {@link TeacherCreationDto}.
     * This method validates the input DTO and creates a new {@link Teacher} entity in the database.
     * The newly created teacher is returned as a {@link TeacherDto} for further use.
     *
     * @param dto the {@link TeacherCreationDto} containing the data for creating a new teacher.
     *            Must not be {@code null} and must be valid.
     * @return a {@link TeacherDto} representing the newly saved teacher.
     */
    TeacherDto save(@NotNull @Valid TeacherCreationDto dto);

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
     * Retrieves a paginated list of teachers filtered by email.
     *
     * @param email    the email address to filter teachers by.
     * @param pageable the {@link Pageable} object specifying pagination and sorting information.
     * @return a {@link Page} containing a list of {@link TeacherDto} objects matching the email filter.
     */
    Page<TeacherDto> getTeacherInPageByEmail(@NotNull String email, @NotNull Pageable pageable);

    /**
     * Updates the password of a teacher.
     *
     * @param passwordUpdateRequest the {@link PasswordUpdateRequestDto} containing the teacher's ID and the new password.
     */
    void updateTeacherPassword(@NotNull PasswordUpdateRequestDto passwordUpdateRequest);

}
