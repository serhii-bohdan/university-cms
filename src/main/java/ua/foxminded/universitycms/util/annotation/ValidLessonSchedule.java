package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.util.validator.LessonValidator;

/**
 * Custom annotation for validating the schedule of a lesson. Ensures that the lesson's start
 * and end times are valid and do not overlap with other lessons on the same study day.
 * <p>
 * This annotation is applied at the class level and is validated by {@link LessonValidator}.
 * The validation checks that the end time is after the start time and ensures that the lesson
 * does not overlap with other lessons scheduled for the same study day.
 *
 * @author Serhii Bohdan
 */
@Documented
@Constraint(validatedBy = {LessonValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLessonSchedule {

    /**
     * Default message displayed when validation fails.
     *
     * @return the validation error message
     */
    String message() default """
        The rules of validity of the lesson time are violated.
        Make sure you have entered the correct data.""";

    /**
     * Allows the specification of validation groups to which this constraint belongs.
     *
     * @return an array of group classes
     */
    Class<?>[] groups() default {};

    /**
     * Can be used to provide custom payload objects to the constraint.
     *
     * @return an array of payload types
     */
    Class<? extends Payload>[] payload() default {};

}
