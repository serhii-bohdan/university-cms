package ua.foxminded.universitycms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.util.Set;

/**
 * Data Transfer Object (DTO) representing a schedule in the university management system.
 * Extends {@link AbstractDto} to inherit an ID and holds a collection of {@link LessonDto} objects
 * defining the schedule's structure. Used for secure and efficient data transfer between layers.
 *
 * @author Serhii Bohdan
 * @see AbstractDto
 * @see LessonDto
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"lessons"})
@SuperBuilder
public class ScheduleDto extends AbstractDto {

    /**
     * The set of lessons that constitute this schedule.
     * Contains a collection of {@link LessonDto} objects representing individual academic events
     * within the schedule.
     */
    private Set<LessonDto> lessons;

}
