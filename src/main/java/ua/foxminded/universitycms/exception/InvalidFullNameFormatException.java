package ua.foxminded.universitycms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a provided full name does not adhere to the expected format.
 * <p>
 * This exception is typically used in scenarios where user input or data validation
 * requires a specific structure for full names.
 *
 * @author Serhii Bohdan
 */
@Getter
public class InvalidFullNameFormatException extends RuntimeException {

    /**
     * The HTTP status code associated with this exception, indicating the nature of the error to the client.
     */
    private final HttpStatus httpStatus;

    /**
     * Constructs a new `InvalidFullNameFormatException` with the specified HTTP status code and error message.
     *
     * @param httpStatus the HTTP status code to associate with this exception
     * @param message    a detailed message describing the specific format violation
     */
    public InvalidFullNameFormatException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
