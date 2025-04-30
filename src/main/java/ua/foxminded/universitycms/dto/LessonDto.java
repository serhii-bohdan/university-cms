package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import ua.foxminded.universitycms.util.annotation.ValidLessonSchedule;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) representing a lesson within a schedule in the university system.
 * Extends {@link AbstractDto} to inherit an ID and holds lesson details like start/end times,
 * time zone offset, course ID, and schedule ID. Uses validation constraints like
 * {@link ValidLessonSchedule}, {@link NotNull}, {@link NotBlank}, and {@link Size} for data integrity.
 *
 * @author Serhii Bohdan
 * @see AbstractDto
 * @see ValidLessonSchedule
 * @see jakarta.validation.constraints.NotNull
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.Size
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@ValidLessonSchedule
public class LessonDto extends AbstractDto {

    /**
     * The date when the lesson takes place.
     * This mandatory field, enforced by {@link NotNull}, defines the specific date of the lesson in the format "yyyy-MM-dd",
     * as specified by {@link DateTimeFormat}.
     */
    @NotNull(message = "Lesson date is mandatory")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * The start time of the lesson.
     * Mandatory field, enforced by {@link NotNull}, representing the local time the lesson begins,
     * adjusted by {@code zoneOffset}.
     */
    @NotNull(message = "Lesson start time is mandatory")
    private LocalTime lessonStartTime;

    /**
     * The end time of the lesson.
     * Mandatory field, enforced by {@link NotNull}, representing the local time the lesson ends,
     * adjusted by {@code zoneOffset}.
     */
    @NotNull(message = "Lesson end time is mandatory")
    private LocalTime lessonEndTime;

    /**
     * The time zone offset for the lesson.
     * Mandatory field, limited to 7 characters (e.g., "+02:00"), enforced by {@link NotBlank} and
     * {@link Size}. Specifies the UTC offset for the lesson's times.
     */
    @NotBlank(message = "Zone offset is mandatory")
    @Size(max = 7)
    private String zoneOffset;

    /**
     * The ID of the course associated with this lesson.
     * Mandatory field, enforced by {@link NotNull}, identifying the course tied to this lesson.
     */
    @NotNull(message = "The course must be specified")
    private Long courseId;

    /**
     * The name of the course associated with this lesson.
     */
    private String courseName;

    /**
     * The ID of the schedule to which this lesson belongs.
     * Mandatory field, enforced by {@link NotNull}, identifying the schedule containing this lesson.
     */
    @NotNull
    private Long scheduleId;

}
