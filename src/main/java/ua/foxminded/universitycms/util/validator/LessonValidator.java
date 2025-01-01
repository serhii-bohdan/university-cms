package ua.foxminded.universitycms.util.validator;

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
 * Validator for the {@link ValidLessonSchedule} annotation. Ensures that a lesson's schedule follows specific rules
 * and does not conflict with other lessons.
 * <p>
 * The validator checks if the end time of the lesson is after the start time and ensures there is no overlap
 * with existing lessons on the same study day. If the rules are violated, appropriate error messages are generated.
 *
 * @author Serhii Bohdan
 */
@Component
@RequiredArgsConstructor
public class LessonValidator implements ConstraintValidator<ValidLessonSchedule, LessonDto> {

    /**
     * Message displayed when the lesson's end time is not after its start time.
     */
    private static final String INVALID_LESSON_TIME_MESSAGE = """
        Invalid lesson time limits. The end time should be after the
        start time of the new lesson.
        """;

    /**
     * Message displayed when the lesson overlaps with another lesson on the same study day.
     */
    private static final String LESSONS_OVERLAPPING_MESSAGE = """
        The lesson you are trying to add overlaps with an existing
        lesson. Change the time limits of the lesson.
        """;

    /**
     * Repository for performing database operations related to {@link Lesson} entities.
     */
    private final LessonRepository lessonRepository;

    /**
     * The {@link LessonDto} object being validated.
     * Stores the current value being checked for constraints.
     */
    private LessonDto value;

    /**
     * The context in which the constraint validation is executed.
     * Used to build and add custom constraint violation messages.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator for the {@link ValidLessonSchedule} annotation.
     * This method is invoked before the validation logic is applied, allowing for any
     * necessary setup or initialization.
     *
     * @param constraintAnnotation the annotation instance being validated
     */
    @Override
    public void initialize(ValidLessonSchedule constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the {@link LessonDto} object against the rules defined in the {@link ValidLessonSchedule} annotation.
     *
     * @param value   the {@link LessonDto} object to be validated
     * @param context the context in which the validation is performed
     * @return {@code true} if the lesson is valid; {@code false} otherwise
     */
    @Override
    public boolean isValid(LessonDto value, ConstraintValidatorContext context) {
        if (value == null || value.getLessonStartTime() == null || value.getLessonEndTime() == null) {
            return false;
        }

        this.value = value;
        this.context = context;

        return isEndTimeAfterStartTime() && !areLessonsOverlapping();
    }

    private boolean isEndTimeAfterStartTime() {
        boolean isValid = value.getLessonEndTime().isAfter(value.getLessonStartTime());
        addConstraintViolationMessageIfInvalid(!isValid, INVALID_LESSON_TIME_MESSAGE);
        return isValid;
    }

    private boolean areLessonsOverlapping() {
        List<Lesson> studyDayLessons = lessonRepository.findLessonByStudyDayId(value.getStudyDayId());

        boolean isOverlapping = studyDayLessons.stream()
            .filter(existingLesson -> !existingLesson.getId().equals(value.getId()))
            .filter(existingLesson -> existingLesson.getTimezone().equals(value.getTimezone()))
            .anyMatch(this::isTimeOverlapping);

        addConstraintViolationMessageIfInvalid(isOverlapping, LESSONS_OVERLAPPING_MESSAGE);
        return isOverlapping;
    }

    private boolean isTimeOverlapping(Lesson existingLesson) {
        return (value.getLessonEndTime().isAfter(existingLesson.getLessonStartTime()) && value.getLessonEndTime().isBefore(existingLesson.getLessonEndTime())) ||
            (value.getLessonStartTime().isAfter(existingLesson.getLessonStartTime()) && value.getLessonStartTime().isBefore(existingLesson.getLessonEndTime()))
            || value.getLessonStartTime().equals(existingLesson.getLessonStartTime()) || value.getLessonEndTime().equals(existingLesson.getLessonEndTime());
    }

    private void addConstraintViolationMessageIfInvalid(boolean isInvalid, String message) {
        if (isInvalid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        }
    }

}
