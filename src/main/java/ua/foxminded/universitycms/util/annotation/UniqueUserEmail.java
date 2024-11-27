package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.util.validator.UniqueUserEmailValidator;

/**
 * Annotation for ensuring the uniqueness of a user's email address.
 * <p>
 * This annotation is applied at the class level to validate that the email provided by a user
 * is unique across the system. It uses the {@link UniqueUserEmailValidator} to check if an
 * existing user is already registered with the given email.
 * <p>
 * If the email is not unique, a default or custom error message will be generated. This
 * annotation can be applied to any class that contains an email field requiring uniqueness validation.
 *
 * @author Serhii Bohdan
 */
@Documented
@Constraint(validatedBy = {UniqueUserEmailValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUserEmail {

    /**
     * Specifies the default error message when the email uniqueness validation fails.
     *
     * @return the error message
     */
    String message() default """
        A user with this email already exists. Please enter another email.
        """;

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
