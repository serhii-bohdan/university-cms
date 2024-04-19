package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
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
    @NotNull
    private CourseDto course;

    /**
     * The ID of the study day that this lesson belongs to within a schedule.
     */
    @NotNull
    @Min(1)
    private Long studyDayId;

    /**
     * Constructs a new {@code LessonDto} instance with the specified details about the lesson.
     *
     * @param lessonStartTime the time the lesson starts on the specified date (considering the time zone)
     * @param lessonEndTime   the time the lesson ends on the specified date (considering the time zone)
     * @param timezone        the time zone identifier (e.g., "Europe/Kiev", "America/Los_ Angeles") for the lesson times
     * @param course          a {@link CourseDto} object representing the course that this lesson covers
     * @param studyDayId      the ID of the study day that this lesson belongs to within a schedule
     */
    public LessonDto(LocalTime lessonStartTime, LocalTime lessonEndTime, String timezone, CourseDto course, Long studyDayId) {
        this.lessonStartTime = lessonStartTime;
        this.lessonEndTime = lessonEndTime;
        this.timezone = timezone;
        this.course = course;
        this.studyDayId = studyDayId;
    }

}
