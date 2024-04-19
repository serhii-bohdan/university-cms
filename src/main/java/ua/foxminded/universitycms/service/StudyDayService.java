package ua.foxminded.universitycms.service;

import jakarta.validation.constraints.NotNull;
import ua.foxminded.universitycms.dto.StudyDayDto;
import ua.foxminded.universitycms.model.StudyDay;
import java.time.LocalDate;
import java.util.Optional;

/**
 * The {@code StudyDayService} interface defines a set of operations for managing {@link StudyDay} entities and their
 * corresponding {@link StudyDayDto} representations. It extends the generic {@link Service} interface, providing
 * specialized services for working with study days, including retrieving study days by schedule ID and date.
 *
 * @author Serhii Bohdan
 */
public interface StudyDayService extends Service<StudyDay, StudyDayDto> {

    /**
     * Retrieves a study day by the ID of the schedule it belongs to and the specific date.
     *
     * @param scheduleId the ID of the schedule
     * @param date       the date of the study day (LocalDate)
     * @return an {@link Optional} containing a {@link StudyDayDto} if a matching study day exists,
     * or {@link Optional#empty()} if no study day is found for the given parameters.
     */
    Optional<StudyDayDto> getStudyDayByScheduleIdAndDate(long scheduleId, @NotNull LocalDate date);

}
