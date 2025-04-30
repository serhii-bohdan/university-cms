package ua.foxminded.universitycms.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.exception.InvalidUserRoleException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Lesson;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.LessonRepository;
import ua.foxminded.universitycms.service.LessonService;

/**
 * Implementation of the {@link LessonService} interface for managing {@link Lesson} entities in the university
 * management system.
 * <p>
 * This service class extends {@link AbstractService} to leverage common CRUD operations and provides a concrete
 * implementation for retrieving user-specific course data based on their role. It uses {@link CourseRepository}
 * to fetch courses and a {@link Mapper} for entity-DTO conversions. The {@code @Service} annotation marks it as
 * a Spring-managed bean, and {@code @Validated} enables validation.
 *
 * @author Serhii Bohdan
 * @see LessonService
 * @see AbstractService
 * @see CourseRepository
 * @see Mapper
 */
@Service
@Validated
public class LessonServiceImpl extends AbstractService<Lesson, LessonDto> implements LessonService {

    /**
     * Error message template for invalid user role exceptions.
     */
    private static final String INCORRECT_USER_ROLE_MESSAGE = "Incorrect user role. The %s role does not have the required permissions.";

    /**
     * Repository for accessing {@link Course} entities associated with users.
     * Used to retrieve courses based on user role and ID, facilitating the mapping of course names to IDs.
     */
    private final CourseRepository courseRepository;

    /**
     * Repository for accessing {@link Lesson} entities.
     * Provides data access operations for lessons, including retrieval of lessons by schedule ID and date range,
     * used in the management of lesson-related data within the university system.
     */
    private final LessonRepository lessonRepository;

    /**
     * Constructs a new {@code LessonServiceImpl} instance with the required dependencies.
     * <p>
     * Initializes the parent {@link AbstractService} with the provided repository and mapper, and sets up the
     * {@link CourseRepository} for course-related operations.
     *
     * @param repository       the {@link JpaRepository} for {@link Lesson} entities, providing basic CRUD operations
     * @param mapper           the {@link Mapper} instance for converting between {@link Lesson} and {@link LessonDto}
     *                         objects
     * @param courseRepository the {@link CourseRepository} for managing course entities
     */
    public LessonServiceImpl(JpaRepository<Lesson, Long> repository, Mapper<Lesson, LessonDto> mapper,
                             CourseRepository courseRepository) {
        super(repository, mapper);
        this.courseRepository = courseRepository;
        this.lessonRepository = (LessonRepository) repository;
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidUserRoleException if the provided {@code userRole} is not supported
     */
    @Override
    public Map<String, Long> getUserCourses(@NotNull Long userId, @NotNull RoleName userRole) {
        List<Course> userCourses = switch (userRole) {
            case STUDENT -> courseRepository.findStudentCoursesByStudentId(userId);
            case TEACHER -> courseRepository.findByAuthorId(userId);
            default ->
                throw new InvalidUserRoleException(HttpStatus.FORBIDDEN, INCORRECT_USER_ROLE_MESSAGE.formatted(userRole.name()));
        };

        return userCourses.stream()
            .collect(Collectors.toMap(Course::getCourseName, Course::getId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LessonDto> findLessonsByScheduleIdAndDateRange(long scheduleId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return lessonRepository.findLessonsByScheduleIdAndDateBetween(scheduleId, startDate, endDate, pageable).map(mapper::toDto);
    }

}
