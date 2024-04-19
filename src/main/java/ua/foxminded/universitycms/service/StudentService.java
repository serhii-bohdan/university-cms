package ua.foxminded.universitycms.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.model.Student;
import java.util.List;

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
     * Retrieves a page of student data containing all students. This method retrieves
     * a paginated list of all students from the underlying data store. It utilizes the provided
     * `Pageable` object to specify the page number, size.
     *
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return a Page object containing a list of StudentDto objects representing the requested page of students
     */
    Page<StudentDto> getStudentsPage(@NotNull Pageable pageable);

    /**
     * Retrieves a page of student data filtered by full name. This method retrieves a paginated list of
     * students whose full names contain (case-insensitive) the provided keyword. It utilizes the `Pageable`
     * object to specify the page number, size.
     *
     * @param fullName the keyword to filter students by full name (can be blank)
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return a Page object containing a list of StudentDto objects representing the requested page of filtered students
     */
    Page<StudentDto> getStudentInPageByName(@NotNull String fullName, @NotNull Pageable pageable);

    /**
     * Retrieves a list of all student names in the system.
     *
     * @return a list of student names
     */
    List<String> getAllNamesOfStudents();

}
