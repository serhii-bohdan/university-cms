package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.util.validator.PasswordUpdateRequestValidator;

/**
 * Annotation to enforce validation rules for a password update request in the university management system.
 * <p>
 * This annotation is applied at the class level to validate a {@link PasswordUpdateRequestDto} object, ensuring
 * that the current password is correct, the new password matches its confirmation, and the new password differs from
 * the current one. The validation logic is implemented in {@link PasswordUpdateRequestValidator}, which performs these
 * checks. As part of the Jakarta Bean Validation framework, this annotation is processed at runtime to ensure the
 * integrity of password update requests.
 *
 * @author Serhii Bohdan
 * @see PasswordUpdateRequestValidator
 * @see PasswordUpdateRequestDto
 * @see jakarta.validation.Constraint
 * @see java.lang.annotation.ElementType#TYPE
 * @see java.lang.annotation.RetentionPolicy#RUNTIME
 */
@Documented
@Constraint(validatedBy = {PasswordUpdateRequestValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPasswordUpdateRequest {

    /**
     * Specifies the default error message returned when the password update validation fails.
     * <p>
     * This message is used if no custom message is provided, indicating issues such as an incorrect current password,
     * mismatched new password and confirmation, or identical current and new passwords. It can be overridden by
     * specifying a different value in the annotation usage.
     *
     * @return the default error message as a {@code String}
     */
    String message() default """
        Password update validation failed. Make sure
        you have entered the correct data.""";

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
     * Can be used by clients of the validation API to attach custom data to the constraint.
     * By default, no payloads are specified.
     *
     * @return an array of payload classes implementing {@link Payload}
     */
    Class<? extends Payload>[] payload() default {};

}
