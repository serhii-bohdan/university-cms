package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.util.validator.CourseValidator;

/**
 * Annotation to enforce uniqueness of a teacher's course within the university management system.
 * <p>
 * This annotation is applied at the class level to ensure that a course, whether being created or updated by a teacher,
 * adheres to uniqueness criteria defined in the system, preventing duplicate course entries for a specific teacher.
 * The validation logic is implemented in {@link CourseValidator}, which checks conditions such as course name and
 * description. As part of the Jakarta Bean Validation framework, this annotation is processed at runtime to validate
 * course-related data.
 *
 * @author Serhii Bohdan
 * @see CourseValidator
 * @see jakarta.validation.Constraint
 * @see java.lang.annotation.ElementType#TYPE
 * @see java.lang.annotation.RetentionPolicy#RUNTIME
 */
@Documented
@Constraint(validatedBy = {CourseValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTeacherCourse {

    /**
     * Specifies the default error message returned when the course uniqueness validation fails.
     * <p>
     * This message is used if no custom message is provided, indicating a violation of the uniqueness constraint.
     * It can be overridden by specifying a different value in the annotation usage.
     *
     * @return the default error message as a {@code String}
     */
    String message() default """
        Rules of uniqueness are violated. Make sure
        you have entered the correct data.""";

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
