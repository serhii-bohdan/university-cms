package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.util.validator.LessonValidator;

/**
 * Annotation to enforce validity and non-overlapping scheduling of a lesson within the university management system.
 * <p>
 * This annotation is applied at the class level to ensure that a lesson's start and end times, as provided in a data
 * object, are valid (end time after start time) and do not overlap with other lessons scheduled on the same study day.
 * The validation logic is implemented in {@link LessonValidator}, which performs the necessary checks. As part of the
 * Jakarta Bean Validation framework, this annotation is processed at runtime to validate lesson scheduling constraints.
 *
 * @author Serhii Bohdan
 * @see LessonValidator
 * @see jakarta.validation.Constraint
 * @see java.lang.annotation.ElementType#TYPE
 * @see java.lang.annotation.RetentionPolicy#RUNTIME
 */
@Documented
@Constraint(validatedBy = {LessonValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLessonSchedule {

    /**
     * Specifies the default error message returned when the lesson schedule validation fails.
     * <p>
     * This message is used if no custom message is provided, indicating a violation of scheduling rules such as invalid
     * time ranges or overlaps. It can be overridden by specifying a different value in the annotation usage.
     *
     * @return the default error message as a {@code String}
     */
    String message() default """
        The rules of validity of the lesson time are violated.
        Make sure you have entered the correct data.""";

    /**
     * Defines the validation groups to which this constraint belongs.
     * <p>
     * Allows grouping of validation constraints for conditional or contextual validation scenarios. By default, no
     * groups are specified.
     *
     * @return an array of validation group classes
     */
    Class<?>[] groups() default {};

    /**
     * Provides custom payload objects for extending the constraint with additional metadata.
     * <p>
     * Can be used by clients of the validation API to attach custom data to the constraint. By default, no payloads
     * are specified.
     *
     * @return an array of payload classes implementing {@link Payload}
     */
    Class<? extends Payload>[] payload() default {};

}
