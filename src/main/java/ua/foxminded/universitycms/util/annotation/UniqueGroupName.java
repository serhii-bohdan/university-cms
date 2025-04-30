package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.util.validator.GroupValidator;

/**
 * Custom annotation to enforce uniqueness of a group name within the university management system.
 * <p>
 * This annotation is applied at the class level to ensure that a group's identifying attributes, such as its name,
 * are unique across the system, preventing duplicate group entries. Validation is performed by the
 * {@link GroupValidator}, which implements the logic to check for uniqueness. As part of the Jakarta Bean Validation
 * framework, this annotation is processed at runtime to validate group-related data.
 *
 * @author Serhii Bohdan
 * @see GroupValidator
 * @see jakarta.validation.Constraint
 * @see java.lang.annotation.ElementType#TYPE
 * @see java.lang.annotation.RetentionPolicy#RUNTIME
 */
@Documented
@Constraint(validatedBy = {GroupValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueGroupName {

    /**
     * Specifies the default error message returned when the group uniqueness validation fails.
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
     * Allows grouping of validation constraints for conditional or contextual validation
     * scenarios. By default, no groups are specified.
     *
     * @return an array of validation group classes
     */
    Class<?>[] groups() default {};

    /**
     * Provides custom payload objects for extending the constraint with additional metadata.
     * Can be used by clients of the validation API to attach custom data to the constraint. By
     * default, no payloads are specified.
     *
     * @return an array of payload classes implementing {@link Payload}
     */
    Class<? extends Payload>[] payload() default {};

}
