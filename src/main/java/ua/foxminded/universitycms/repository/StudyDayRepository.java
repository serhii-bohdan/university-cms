package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.StudyDay;
import java.time.LocalDate;
import java.util.Optional;

/**
 * The {@code StudyDayRepository} interface is a Spring Data JPA repository for
 * {@link StudyDay} entities.
 * <p>
 * This interface extends {@link JpaRepository}, which provides JPA related
 * methods such as save(), findOne(), findAll(), count(), delete(). This
 * interface is annotated with {@code @Repository}, indicating that it's a
 * "Repository" bean. A Repository is a mechanism for encapsulating storage,
 * retrieval, and search behavior which emulates a collection of objects.
 *
 * @author Serhii Bohdan
 */
@Repository
public interface StudyDayRepository extends JpaRepository<StudyDay, Long> {

    /**
     * Finds a study day with the given schedule ID and date.
     *
     * @param scheduleId the ID of the schedule to search for the study day within
     * @param date       the date of the study day to find
     * @return an {@link Optional} containing the found study day, or an empty Optional if none found
     */
    Optional<StudyDay> findByScheduleIdAndDate(Long scheduleId, LocalDate date);

}
