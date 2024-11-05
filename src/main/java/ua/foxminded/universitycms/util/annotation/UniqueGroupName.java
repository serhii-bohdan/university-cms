package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.util.validator.GroupValidator;

/**
 * Custom annotation used to validate the uniqueness of a group in the system.
 * <p>
 * This annotation is applied at the type level to enforce that a group with the specified
 * parameters (like group name) does not already exist in the system. The validation logic
 * is handled by the {@link GroupValidator}.
 * <p>
 * The default error message is displayed when the uniqueness constraint is violated.
 * It can be customized by overriding the {@code message()} attribute.
 *
 * @author Serhii Bohdan
 */
@Documented
@Constraint(validatedBy = {GroupValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueGroupName {

    /**
     * Specifies the default error message when the group validation fails.
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
