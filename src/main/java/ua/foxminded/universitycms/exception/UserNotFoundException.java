package ua.foxminded.universitycms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested user cannot be found within the system.
 *
 * @author Serhii Bohdan
 */
@Getter
public class UserNotFoundException extends RuntimeException {

    /**
     * The HTTP status code associated with this exception.
     */
    private final HttpStatus httpStatus;

    /**
     * Constructs a new `UserNotFoundException` with the specified HTTP httpStatus and a detailed message.
     *
     * @param httpStatus the HTTP httpStatus code to be associated with this exception
     * @param message    a descriptive message explaining the reason for the exception
     */
    public UserNotFoundException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
