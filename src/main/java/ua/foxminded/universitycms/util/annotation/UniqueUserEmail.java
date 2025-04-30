package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.util.validator.UniqueUserEmailValidator;

/**
 * Annotation to enforce uniqueness of a user's email address within the university management system.
 * <p>
 * This annotation is applied at the class level to ensure that a user's email, as provided in a data object,
 * is unique across all registered users in the system, preventing duplicate email registrations. The validation
 * logic is implemented in {@link UniqueUserEmailValidator}, which checks for existing users with the same email.
 * As part of the Jakarta Bean Validation framework, this annotation is processed at runtime to validate email
 * uniqueness.
 *
 * @author Serhii Bohdan
 * @see UniqueUserEmailValidator
 * @see jakarta.validation.Constraint
 * @see java.lang.annotation.ElementType#TYPE
 * @see java.lang.annotation.RetentionPolicy#RUNTIME
 */
@Documented
@Constraint(validatedBy = {UniqueUserEmailValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUserEmail {

    /**
     * Specifies the default error message returned when the email uniqueness validation fails.
     * <p>
     * This message is used if no custom message is provided, indicating that the email is already
     * registered. It can be overridden by specifying a different value in the annotation usage.
     *
     * @return the default error message as a {@code String}
     */
    String message() default """
        A user with this email already exists. Please enter another email.
        """;

    /**
     * Defines the validation groups to which this constraint belongs.
     * <p>
     * Allows grouping of validation constraints for conditional or contextual validation scenarios.
     * By default, no groups are specified.
     *
     * @return an array of validation group classes
     */
    Class<?>[] groups() default {};

    /**
     * Provides custom payload objects for extending the constraint with additional metadata.
     * <p>
     * Can be used by clients of the validation API to attach custom data to the constraint. By
     * default, no payloads are specified.
     *
     * @return an array of payload classes implementing {@link Payload}
     */
    Class<? extends Payload>[] payload() default {};

}
