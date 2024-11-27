package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mapstruct.Qualifier;

/**
 * Custom annotation used to qualify and map specific password encoder methods in the application.
 * <p>
 * This annotation serves as a qualifier in dependency injection scenarios where a particular
 * password encoder needs to be explicitly specified. It is particularly useful when multiple
 * implementations of a password encoder are available, allowing precise selection of the desired one.
 *
 * @author Serhii Bohdan
 */
@Qualifier
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface PasswordEncoderMapping {
}
