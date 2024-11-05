package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.util.validator.PasswordUpdateRequestValidator;

/**
 * Annotation for validating a password update request.
 * <p>
 * This annotation is used to apply validation rules to a {@link PasswordUpdateRequestDto} object,
 * ensuring that the current password is correct, the new password and its confirmation match,
 * and the current password is not the same as the new password.
 * <p>
 * The validation is performed by the {@link PasswordUpdateRequestValidator} class, which checks
 * the integrity of the data provided in the password update request. This annotation can be applied
 * to classes representing password update requests.
 *
 * @author Serhii Bohdan
 */
@Documented
@Constraint(validatedBy = {PasswordUpdateRequestValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPasswordUpdateRequest {

    /**
     * Error message to be returned when validation fails.
     *
     * @return the error message
     */
    String message() default """
        Password update validation failed. Make sure
        you have entered the correct data.""";

    /**
     * Groups for validation categorization.
     *
     * @return the validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Additional data to be carried along with the annotation.
     *
     * @return the payload
     */
    Class<? extends Payload>[] payload() default {};

}
