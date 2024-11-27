package ua.foxminded.universitycms.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.util.annotation.PasswordEncoderMapping;

/**
 * A component that provides a mapping for password encoding functionality.
 * <p>
 * This class encapsulates the {@link PasswordEncoder} to handle password encoding,
 * offering a single method to encode raw passwords. It is marked with the custom
 * {@link PasswordEncoderMapping} annotation, enabling it to act as a qualified
 * method for password encoding operations.
 *
 * @author Serhii Bohdan
 */
@Component
@RequiredArgsConstructor
public class PasswordEncoderMapper {

    /**
     * The {@link PasswordEncoder} implementation used for encoding passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Encodes the provided raw password.
     *
     * @param rawPassword the raw password to encode
     * @return the encoded password
     */
    @PasswordEncoderMapping
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
