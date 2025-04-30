package ua.foxminded.universitycms.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.util.annotation.PasswordEncoderMapping;

/**
 * Component providing password encoding functionality within the university management system.
 * <p>
 * This class encapsulates a {@link PasswordEncoder} instance to perform secure password encoding operations.
 * It serves as a utility in the security layer, exposing a single method to encode raw passwords. The
 * {@link PasswordEncoderMapping} annotation marks the encoding method as a qualified entry point for
 * password encoding, enhancing modularity. The {@code @Component} annotation registers this class as a
 * Spring-managed bean, and {@code @RequiredArgsConstructor} ensures dependency injection of the encoder.
 *
 * @author Serhii Bohdan
 * @see PasswordEncoder
 * @see PasswordEncoderMapping
 * @see org.springframework.stereotype.Component
 * @see lombok.RequiredArgsConstructor
 */
@Component
@RequiredArgsConstructor
public class PasswordEncoderMapper {

    /**
     * The {@link PasswordEncoder} instance used to encode passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Encodes the provided raw password into a secure hashed format.
     * <p>
     * This method delegates to the underlying {@link PasswordEncoder} to transform the raw password into
     * an encoded string suitable for secure storage. The {@link PasswordEncoderMapping} annotation identifies
     * this method as the designated entry point for password encoding operations.
     *
     * @param rawPassword the raw, unencoded password to be processed
     * @return the encoded password as a {@code String}
     */
    @PasswordEncoderMapping
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
