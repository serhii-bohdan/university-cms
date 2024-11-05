package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.util.validator.StudentValidator;

/**
 * Annotation for validating the uniqueness of a student entity.
 * <p>
 * This annotation can be applied to a {@link StudentDto} class to ensure that
 * the rules of uniqueness are upheld during the validation process.
 * It utilizes the {@link StudentValidator} class to implement the validation logic.
 *
 * @author Serhii Bohdan
 */
@Documented
@Constraint(validatedBy = {StudentValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueStudentEmail {

    /**
     * Custom error message that will be returned when validation fails.
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
