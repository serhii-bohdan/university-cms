package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.util.validator.UniqueGroupValidator;

/**
 * Custom annotation to validate the uniqueness of a group entity.
 * <p>
 * This annotation is used to ensure that no duplicate group entries exist
 * based on specific criteria. It is typically applied at the class level and
 * validated using the {@link UniqueGroupValidator}.
 *
 * @author Serhii Bohdan
 */
@Documented
@Constraint(validatedBy = {UniqueGroupValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueGroup {

    /**
     * Error message to be returned if the group is not unique.
     *
     * @return the default error message
     */
    String message() default """
        Rules of uniqueness are violated.
        A group with these parameters already exists.""";

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
