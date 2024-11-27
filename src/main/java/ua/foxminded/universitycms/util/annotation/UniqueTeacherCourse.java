package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.util.validator.CourseValidator;

/**
 * Annotation for validating the uniqueness of a teacher's course.
 * <p>
 * This annotation ensures that a course created or updated by a teacher meets the uniqueness criteria.
 * The validation logic is implemented in the {@link CourseValidator} class, which checks the necessary
 * conditions.
 * <p>
 * This annotation is applied at the class level and can be used with any class representing a teacher's
 * course. If the validation fails, a default or custom error message is returned.
 *
 * @author Serhii Bohdan
 */
@Documented
@Constraint(validatedBy = {CourseValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTeacherCourse {

    /**
     * Specifies the default error message when the course validation fails.
     *
     * @return the error message
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
