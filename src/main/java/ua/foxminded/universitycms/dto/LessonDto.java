package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.util.annotation.ValidLessonSchedule;
import java.time.LocalTime;

/**
 * The {@code LessonDto} class is a concrete DTO (Data Transfer Object) that extends the {@link AbstractDto} class.
 * It represents a lesson entity within a schedule, containing information about the lesson time, course,
 * and the study day it's associated with.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@ValidLessonSchedule
public class LessonDto extends AbstractDto {

    /**
     * The time the lesson starts on the specified date (considering the time zone).
     */
    @NotNull(message = "Lesson start time is mandatory")
    private LocalTime lessonStartTime;

    /**
     * The time the lesson ends on the specified date (considering the time zone).
     */
    @NotNull(message = "Lesson start time is mandatory")
    private LocalTime lessonEndTime;

    /**
     * The time zone identifier for the lesson times.
     */
    @NotBlank(message = "Time zone is mandatory")
    @Size(max = 255)
    private String timezone;

    /**
     * A {@link CourseDto} object representing the course that this lesson covers.
     */
    @NotNull(message = "The course must be specified")
    private CourseDto course;

    /**
     * The ID of the study day that this lesson belongs to within a schedule.
     */
    @NotNull
    private Long studyDayId;

}
