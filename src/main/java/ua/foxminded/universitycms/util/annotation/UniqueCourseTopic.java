package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.util.validator.TopicValidator;

/**
 * Annotation to enforce the uniqueness of a course topic.
 * <p>
 * This annotation can be applied to classes to ensure that the course topic adheres
 * to defined rules of uniqueness. It is validated by the {@link TopicValidator}, which
 * contains the logic for checking the uniqueness.
 *
 * @author Serhii Bohdan
 */
@Documented
@Constraint(validatedBy = TopicValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCourseTopic {

    /**
     * Specifies the default error message when the topic validation fails.
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
