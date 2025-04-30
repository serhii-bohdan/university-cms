package ua.foxminded.universitycms.service;

import java.time.LocalDate;
import java.util.Map;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.model.Lesson;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;

/**
 * Service interface for managing {@link Lesson} entities and their DTO representations in the university management
 * system.
 * <p>
 * This interface extends the generic {@link Service} interface, specializing in operations for {@link Lesson} entities
 * mapped to {@link LessonDto} DTOs. It provides methods for CRUD operations inherited from {@link Service}, along with
 * an additional method for retrieving a user's associated courses as a mapping of course names to IDs. Implementations
 * of this interface handle business logic related to lesson management, leveraging user authentication details for
 * personalized data retrieval.
 *
 * @author Serhii Bohdan
 * @see Service
 * @see Lesson
 * @see LessonDto
 * @see CustomUserDetails
 */
public interface LessonService extends Service<Lesson, LessonDto> {

    /**
     * Retrieves a mapping of course names to their IDs for a specific user.
     * <p>
     * Returns a map where keys are course names and values are course IDs, based on the user's ID and role.
     * The {@link NotNull} constraint ensures that both {@code userId} and {@code userRole} are provided.
     *
     * @param userId   the ID of the user, must be non-null
     * @param userRole the role of the user, must be non-null
     * @return a {@link Map} of course names to course IDs associated with the user
     */
    Map<String, Long> getUserCourses(@NotNull Long userId, @NotNull RoleName userRole);

    /**
     * Retrieves a paginated list of lessons for a schedule within a date range.
     * Returns a {@link Page} of {@link LessonDto} objects for the specified {@code scheduleId}, with dates
     * between {@code startDate} and {@code endDate} (inclusive), using {@code pageable} for pagination.
     * All parameters are mandatory, enforced by {@link NotNull}.
     *
     * @param scheduleId the ID of the schedule to filter lessons by
     * @param startDate  the start date of the range (inclusive), must be non-null
     * @param endDate    the end date of the range (inclusive), must be non-null
     * @param pageable   the pagination and sorting configuration, must be non-null
     * @return a {@link Page} of {@link LessonDto} objects matching the criteria
     */
    Page<LessonDto> findLessonsByScheduleIdAndDateRange(long scheduleId, @NotNull LocalDate startDate, @NotNull LocalDate endDate, @NotNull Pageable pageable);

}
