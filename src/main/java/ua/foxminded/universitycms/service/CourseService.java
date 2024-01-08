package ua.foxminded.universitycms.service;

import java.util.Optional;
import ua.foxminded.universitycms.dto.CourseDto;

/**
 * The {@code CourseService} interface provides methods for managing courses.
 * <p>
 * This interface includes methods for adding a course, getting a course by ID,
 * and deleting a course by ID.
 *
 * @author Serhii Bohdan
 */
public interface CourseService {

    /**
     * Adds a new course.
     *
     * @param courseDto the course DTO to add
     * @return true if the course was added successfully, false otherwise
     */
    boolean addCourse(CourseDto courseDto);

    /**
     * Gets a course by ID.
     *
     * @param courseId the ID of the course to get
     * @return an Optional containing the course DTO if found, an empty Optional
     *         otherwise
     */
    Optional<CourseDto> getCourseById(Long courseId);

    /**
     * Deletes a course by ID.
     *
     * @param courseId the ID of the course to delete
     * @return true if the course was deleted successfully, false otherwise
     */
    boolean deleteCourseById(Long courseId);

}