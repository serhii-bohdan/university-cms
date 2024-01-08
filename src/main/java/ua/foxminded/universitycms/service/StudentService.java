package ua.foxminded.universitycms.service;

import java.util.Optional;
import ua.foxminded.universitycms.dto.StudentDto;

/**
 * The {@code StudentService} interface provides methods for managing students.
 * <p>
 * This interface includes methods for adding a student, getting a student by
 * ID, and deleting a student by ID.
 *
 * @author Serhii Bohdan
 */
public interface StudentService {

    /**
     * Adds a new student.
     *
     * @param studentDto the student DTO to add
     * @return true if the student was added successfully, false otherwise
     */
    boolean addStudent(StudentDto studentDto);

    /**
     * Gets a student by ID.
     *
     * @param studentId the ID of the student to get
     * @return an Optional containing the student DTO if found, an empty Optional
     *         otherwise
     */
    Optional<StudentDto> getStudentById(Long studentId);

    /**
     * Deletes a student by ID.
     *
     * @param studentId the ID of the student to delete
     * @return true if the student was deleted successfully, false otherwise
     */
    boolean deleteStudentById(Long studentId);

}
