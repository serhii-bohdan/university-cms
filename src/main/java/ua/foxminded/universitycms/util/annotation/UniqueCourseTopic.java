package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.universitycms.util.validator.TopicValidator;

/**
 * Annotation to enforce uniqueness constraints on course topics within the university management system.
 * <p>
 * This annotation is applied at the class level to ensure that a course topic adheres to uniqueness rules defined
 * in the system, preventing duplicate topics within a course. It is validated by {@link TopicValidator}, which
 * implements the specific logic to check uniqueness. The annotation is part of the Jakarta Bean Validation framework,
 * allowing integration with validation processes at runtime.
 *
 * @author Serhii Bohdan
 * @see TopicValidator
 * @see jakarta.validation.Constraint
 * @see java.lang.annotation.ElementType#TYPE
 * @see java.lang.annotation.RetentionPolicy#RUNTIME
 */
@Documented
@Constraint(validatedBy = {TopicValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCourseTopic {

    /**
     * Specifies the default error message returned when the uniqueness validation fails.
     * This message is displayed if no custom message is provided, indicating a violation of the
     * uniqueness rules for the topic.
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
     * Can be used by clients of the validation API to attach custom data to the constraint. By default,
     * no payloads are specified.
     *
     * @return an array of payload classes implementing {@link Payload}
     */
    Class<? extends Payload>[] payload() default {};

}
