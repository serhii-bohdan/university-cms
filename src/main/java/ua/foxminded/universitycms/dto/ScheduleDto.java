package ua.foxminded.universitycms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.util.Set;

/**
 * The {@code ScheduleDto} class is a concrete DTO (Data Transfer Object) that extends the {@link AbstractDto} class.
 * It represents a schedule entity in the system, containing a collection of study days with their lessons.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "studyDays")
@SuperBuilder
public class ScheduleDto extends AbstractDto {

    /**
     * A collection of {@link StudyDayDto} objects representing individual days within the schedule,
     * each with their associated lessons.
     */
    private Set<StudyDayDto> studyDays;

}
