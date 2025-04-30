package ua.foxminded.universitycms.util.validator;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.model.Lesson;
import ua.foxminded.universitycms.repository.LessonRepository;
import ua.foxminded.universitycms.util.annotation.ValidLessonSchedule;

/**
 * Validator ensuring valid and non-overlapping lesson scheduling in the university system.
 * Implements {@link ConstraintValidator} to enforce {@link ValidLessonSchedule} on
 * {@link LessonDto} objects. Checks start/end time consistency, future scheduling, and
 * overlap with existing lessons via {@link LessonRepository}. Registered as a Spring bean
 * with {@code @Component} and uses {@code @RequiredArgsConstructor} for dependency injection.
 *
 * @author Serhii Bohdan
 * @see ConstraintValidator
 * @see ValidLessonSchedule
 * @see LessonDto
 * @see LessonRepository
 */
@Component
@RequiredArgsConstructor
public class LessonValidator implements ConstraintValidator<ValidLessonSchedule, LessonDto> {

    /**
     * Error message for invalid lesson time limits (end time not after start time).
     */
    private static final String INVALID_LESSON_TIME_MESSAGE = """
        Invalid lesson time limits. The end time should be after the
        start time of the new lesson.
        """;

    /**
     * Error message for overlapping lessons on the same schedule and date.
     */
    private static final String LESSONS_OVERLAPPING_MESSAGE = """
        The lesson you are trying to add overlaps with an existing
        lesson. Change the time limits of the lesson.
        """;

    /**
     * Error message for lessons not scheduled in the future.
     */
    private static final String LESSON_NOT_ACTUAL_MESSAGE = """
        The lesson you are trying to schedule is not relevant in the context
        of time. Select a time period in the future.
        """;

    /**
     * Repository for querying {@link Lesson} data to check overlapping schedules.
     */
    private final LessonRepository lessonRepository;

    /**
     * The {@link LessonDto} object currently being validated.
     */
    private LessonDto value;

    /**
     * The context for reporting constraint violations during validation.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator with the {@link ValidLessonSchedule} annotation.
     * Delegates to the default implementation without additional setup.
     *
     * @param constraintAnnotation the {@link ValidLessonSchedule} annotation instance
     */
    @Override
    public void initialize(ValidLessonSchedule constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates scheduling rules for a lesson in a {@link LessonDto}.
     * Ensures the start time precedes the end time, the lesson is in the future, and it does not overlap
     * with existing lessons for the same schedule, considering time zones. Returns {@code true} if valid,
     * {@code false} otherwise, adding error messages to {@link ConstraintValidatorContext}.
     *
     * @param value   the {@link LessonDto} to validate
     * @param context the {@link ConstraintValidatorContext} for reporting errors
     * @return {@code true} if the lesson schedule is valid and non-overlapping; {@code false} otherwise
     */
    @Override
    public boolean isValid(LessonDto value, ConstraintValidatorContext context) {
        if (isLessonDtoInvalid(value)) {
            return false;
        }

        this.value = value;
        this.context = context;

        return isEndTimeAfterStartTime() && isNewLessonActual() && !areLessonsOverlapping();
    }

    private boolean isLessonDtoInvalid(LessonDto value) {
        return value == null || value.getDate() == null || value.getLessonStartTime() == null || value.getLessonEndTime() == null;
    }

    private boolean isEndTimeAfterStartTime() {
        boolean isValid = value.getLessonEndTime().isAfter(value.getLessonStartTime());
        addConstraintViolationMessageIfInvalid(!isValid, INVALID_LESSON_TIME_MESSAGE);
        return isValid;
    }

    private boolean isNewLessonActual() {
        ZoneOffset zoneOffset = ZoneOffset.of(value.getZoneOffset());
        ZonedDateTime currentTime = ZonedDateTime.now(zoneOffset);
        ZonedDateTime lessonStartTime = ZonedDateTime.of(value.getDate(), value.getLessonStartTime(), zoneOffset);

        boolean isActual = lessonStartTime.isAfter(currentTime);
        addConstraintViolationMessageIfInvalid(!isActual, LESSON_NOT_ACTUAL_MESSAGE);
        return isActual;
    }

    private boolean areLessonsOverlapping() {
        List<Lesson> studyDayLessons = lessonRepository.findLessonsByScheduleId(value.getScheduleId());
        boolean isOverlapping = studyDayLessons.stream()
            .filter(existingLesson -> !existingLesson.getId().equals(value.getId()))
            .filter(existingLesson -> {
                ZoneOffset zoneOffset = ZoneOffset.of(existingLesson.getZoneOffset());
                ZonedDateTime currentTime = ZonedDateTime.now(zoneOffset);
                ZonedDateTime lessonStartTime = ZonedDateTime.of(existingLesson.getDate(), existingLesson.getLessonStartTime(), zoneOffset);
                return lessonStartTime.isAfter(currentTime);
            })
            .anyMatch(this::isTimeOverlapping);

        addConstraintViolationMessageIfInvalid(isOverlapping, LESSONS_OVERLAPPING_MESSAGE);
        return isOverlapping;
    }

    private boolean isTimeOverlapping(Lesson existingLesson) {
        ZoneOffset newLessonZoneOffset = ZoneOffset.of(value.getZoneOffset());
        ZonedDateTime newLessonStartTime = ZonedDateTime.of(value.getDate(), value.getLessonStartTime(), newLessonZoneOffset);
        ZonedDateTime newLessonEndTime = ZonedDateTime.of(value.getDate(), value.getLessonEndTime(), newLessonZoneOffset);

        ZoneOffset existingLessonZoneOffset = ZoneOffset.of(existingLesson.getZoneOffset());
        ZonedDateTime existingLessonStartTime = ZonedDateTime.of(existingLesson.getDate(), existingLesson.getLessonStartTime(), existingLessonZoneOffset);
        ZonedDateTime existingLessonEndTime = ZonedDateTime.of(existingLesson.getDate(), existingLesson.getLessonEndTime(), existingLessonZoneOffset);

        return isEndTimeWithinExistingLesson(newLessonEndTime, existingLessonStartTime, existingLessonEndTime)
            || isStartTimeWithinExistingLesson(newLessonStartTime, existingLessonStartTime, existingLessonEndTime)
            || isStartTimeEqualToExistingLesson(newLessonStartTime, existingLessonStartTime)
            || isEndTimeEqualToExistingLesson(newLessonEndTime, existingLessonEndTime)
            || doesNewLessonEncompassExistingLesson(newLessonStartTime, newLessonEndTime, existingLessonStartTime, existingLessonEndTime);
    }

    private boolean isEndTimeWithinExistingLesson(ZonedDateTime newLessonEndTime, ZonedDateTime existingLessonStartTime,
                                                  ZonedDateTime existingLessonEndTime) {
        return newLessonEndTime.isAfter(existingLessonStartTime) && newLessonEndTime.isBefore(existingLessonEndTime);
    }

    private boolean isStartTimeWithinExistingLesson(ZonedDateTime newLessonStartTime, ZonedDateTime existingLessonStartTime,
                                                    ZonedDateTime existingLessonEndTime) {
        return newLessonStartTime.isAfter(existingLessonStartTime) && newLessonStartTime.isBefore(existingLessonEndTime);
    }

    private boolean isStartTimeEqualToExistingLesson(ZonedDateTime newLessonStartTime, ZonedDateTime existingLessonStartTime) {
        return newLessonStartTime.toInstant().equals(existingLessonStartTime.toInstant());
    }

    private boolean isEndTimeEqualToExistingLesson(ZonedDateTime newLessonEndTime, ZonedDateTime existingLessonEndTime) {
        return newLessonEndTime.toInstant().equals(existingLessonEndTime.toInstant());
    }

    private boolean doesNewLessonEncompassExistingLesson(ZonedDateTime newLessonStartTime, ZonedDateTime newLessonEndTime,
                                                         ZonedDateTime existingLessonStartTime, ZonedDateTime existingLessonEndTime) {
        return newLessonStartTime.isBefore(existingLessonStartTime) && newLessonEndTime.isAfter(existingLessonEndTime);
    }

    private void addConstraintViolationMessageIfInvalid(boolean isInvalid, String message) {
        if (isInvalid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        }
    }

}
