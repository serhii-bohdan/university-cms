package ua.foxminded.universitycms.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mapstruct.Qualifier;
import ua.foxminded.universitycms.security.PasswordEncoderMapper;

/**
 * Custom qualifier annotation for identifying specific password encoding methods in the university
 * management system.
 * <p>
 * This annotation is used in conjunction with MapStruct to mark methods responsible for password
 * encoding, enabling precise selection of the desired encoding logic when multiple implementations are
 * available. It facilitates dependency injection and mapping scenarios by distinguishing a particular
 * {@link PasswordEncoderMapper} method, ensuring that passwords are securely encoded during operations
 * such as user creation or updates. The annotation is applied at the method level and retained at the
 * class level for runtime processing.
 *
 * @author Serhii Bohdan
 * @see org.mapstruct.Qualifier
 * @see java.lang.annotation.ElementType#METHOD
 * @see java.lang.annotation.RetentionPolicy#CLASS
 */
@Qualifier
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface PasswordEncoderMapping {
}
