package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.util.validator.StudyDayValidator;

/**
 * Annotation for validating that a study day is unique within a specific schedule
 * and adheres to scheduling rules.
 * <p>
 * This annotation ensures that the date of the study day is not duplicated within the schedule
 * and that the selected date is valid for scheduling. The validation logic is implemented in the
 * {@link StudyDayValidator} class.
 *
 * @author Serhii Bohdan
 */
@Documented
@Constraint(validatedBy = {StudyDayValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueStudyDayWithinSchedule {

    /**
     * Default message to be returned when the validation fails.
     *
     * @return the default error message
     */
    String message() default """
        Rules of uniqueness are violated. Make sure
        you have entered the correct data.""";

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
