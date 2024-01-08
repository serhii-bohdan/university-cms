package ua.foxminded.universitycms.service;

import java.util.Optional;
import ua.foxminded.universitycms.dto.TeacherDto;

/**
 * The {@code TeacherService} interface provides methods for managing teachers.
 * <p>
 * This interface includes methods for adding a teacher, getting a teacher by
 * ID, and deleting a teacher by ID.
 *
 * @author Serhii Bohdan
 */
public interface TeacherService {

    /**
     * Adds a new teacher.
     *
     * @param teacherDto the teacher DTO to add
     * @return true if the teacher was added successfully, false otherwise
     */
    boolean addTeacher(TeacherDto teacherDto);

    /**
     * Gets a teacher by ID.
     *
     * @param teacherId the ID of the teacher to get
     * @return an Optional containing the teacher DTO if found, an empty Optional
     *         otherwise
     */
    Optional<TeacherDto> getTeacherById(Long teacherId);

    /**
     * Deletes a teacher by ID.
     *
     * @param teacherId the ID of the teacher to delete
     * @return true if the teacher was deleted successfully, false otherwise
     */
    boolean deleteTeacherById(Long teacherId);

}
