package ua.foxminded.universitycms.service;

import java.util.Collection;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.TeacherCreationDto;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.Teacher;

/**
 * Service interface for managing {@link Teacher} entities and their DTO representations in the university management
 * system.
 * <p>
 * This interface extends the generic {@link Service} interface, specializing in operations for {@link Teacher}
 * entities mapped to {@link TeacherDto} DTOs. It provides methods for CRUD operations inherited from {@link Service},
 * along with additional functionality such as creating teachers from creation DTOs, paginated retrieval with email
 * filtering, updating passwords and full names, and extracting teacher emails. Implementations of this interface
 * handle business logic related to teacher management, leveraging validation constraints for data integrity.
 *
 * @author Serhii Bohdan
 * @see Service
 * @see Teacher
 * @see TeacherDto
 * @see TeacherCreationDto
 * @see PasswordUpdateRequestDto
 * @see FullName
 */
public interface TeacherService extends Service<Teacher, TeacherDto> {

    /**
     * Saves a new teacher entity based on the provided creation DTO.
     * <p>
     * Creates a new teacher in the system using the data from the {@link TeacherCreationDto}, applying validation
     * rules ({@link Valid}) to ensure data integrity and the {@link NotNull} constraint to ensure the DTO is provided.
     *
     * @param dto the {@link TeacherCreationDto} containing the data for the new teacher, must be non-null and valid
     * @return a {@link TeacherDto} representing the saved teacher, including its generated ID
     */
    TeacherDto save(@NotNull @Valid TeacherCreationDto dto);

    /**
     * Retrieves a paginated list of teachers, optionally filtered by email.
     * <p>
     * Fetches teachers from the system based on the provided {@link Pageable} paging parameters and an optional email
     * filter. The {@link NotNull} constraint ensures that the paging configuration is provided.
     *
     * @param pageable the paging and sorting configuration for the query, must be non-null
     * @param email    an optional email filter; if null or empty, all teachers are retrieved
     * @return a {@link Page} of {@link TeacherDto} objects representing the filtered and paginated teachers
     */
    Page<TeacherDto> findTeachers(@NotNull Pageable pageable, String email);

    /**
     * Updates a teacher's password based on the provided request.
     * <p>
     * Modifies the password of a teacher identified in the {@link PasswordUpdateRequestDto}, ensuring the request
     * meets validation criteria. The {@link NotNull} constraint ensures that the request DTO is provided.
     *
     * @param passwordUpdateRequest the {@link PasswordUpdateRequestDto} containing the password update details, must
     *                              be non-null
     */
    void updateTeacherPassword(@NotNull PasswordUpdateRequestDto passwordUpdateRequest);

    /**
     * Extracts email addresses from a collection of teachers.
     * <p>
     * Converts the provided collection of {@link TeacherDto} objects into a list of their email addresses.
     * The {@link NotNull} constraint ensures that the input collection is not null.
     *
     * @param teachers the collection of {@link TeacherDto} objects from which to extract emails, must be non-null
     * @return a {@link List} of email addresses as strings
     */
    List<String> extractTeacherEmails(@NotNull Collection<TeacherDto> teachers);

    /**
     * Updates a teacher's full name.
     * <p>
     * Changes the full name of the teacher identified by {@code teacherId} to the provided {@link FullName} object.
     *
     * @param teacherId the ID of the teacher whose full name is to be updated
     * @param fullName  the new {@link FullName} object containing the updated first and last names
     */
    void updateTeacherFullName(long teacherId, FullName fullName);

}
