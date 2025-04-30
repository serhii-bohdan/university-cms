package ua.foxminded.universitycms.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Lesson;

/**
 * Spring Data JPA repository for managing {@link Lesson} entities in the university system.
 * Extends {@link JpaRepository} to provide CRUD operations for {@link Lesson} entities, using
 * {@code Long} as the primary key. Marked with {@code @Repository} to enable Spring's automatic
 * implementation of data access logic.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Lesson
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    /**
     * Retrieves a list of lessons associated with a specific schedule.
     * Returns all {@link Lesson} entities linked to the specified {@code scheduleId}.
     *
     * @param scheduleId the ID of the schedule to filter lessons by
     * @return a {@link List} of {@link Lesson} entities matching the schedule ID
     */
    List<Lesson> findLessonsByScheduleId(long scheduleId);

    /**
     * Retrieves a paginated list of lessons for a schedule within a date range.
     * Returns a {@link Page} of {@link Lesson} entities associated with the specified {@code scheduleId}
     * and occurring between {@code startDate} and {@code endDate}, using {@code pageable} for pagination.
     *
     * @param scheduleId the ID of the schedule to filter lessons by
     * @param startDate  the start date of the range (inclusive)
     * @param endDate    the end date of the range (inclusive)
     * @param pageable   the pagination and sorting configuration
     * @return a {@link Page} of {@link Lesson} entities matching the criteria
     */
    Page<Lesson> findLessonsByScheduleIdAndDateBetween(long scheduleId, LocalDate startDate, LocalDate endDate, Pageable pageable);

}
