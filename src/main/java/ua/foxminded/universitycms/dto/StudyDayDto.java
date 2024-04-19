package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

/**
 * The {@code StudyDayDto} class is a concrete DTO (Data Transfer Object) that extends the {@link AbstractDto} class.
 * It represents a day within a schedule entity in the system, containing information about the date, week day,
 * associated lessons, and the schedule it belongs to.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "lessons")
@SuperBuilder
public class StudyDayDto extends AbstractDto {

    /**
     * The date of the study day.
     */
    @NotNull(message = "Date is mandatory")
    private LocalDate date;

    /**
     * The day of the week for the study day (e.g., MONDAY, TUESDAY, etc.).
     */
    @NotNull(message = "Day of week is mandatory")
    private DayOfWeek weekDay;

    /**
     * The ID of the schedule that this study day belongs to.
     */
    @NotNull
    @Min(1)
    private Long scheduleId;

    /**
     * A collection of {@link LessonDto} objects representing the lessons on this particular study day.
     */
    private Set<LessonDto> lessons;

    /**
     * Constructs a new {@code StudyDayDto} instance with the specified details about the study day.
     *
     * @param date       the date of the study day
     * @param weekDay    the day of the week for the study day
     * @param scheduleId the ID of the schedule that this study day belongs to
     */
    public StudyDayDto(LocalDate date, DayOfWeek weekDay, Long scheduleId) {
        this.date = date;
        this.weekDay = weekDay;
        this.scheduleId = scheduleId;
    }

}
