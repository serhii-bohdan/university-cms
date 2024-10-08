package ua.foxminded.universitycms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * A custom exception representing a validation error that occurred during the processing of a request.
 * <p>
 * This exception is a subclass of {@link RuntimeException} and provides the HTTP status code associated with the error.
 * It is typically used to indicate that a client-side error has occurred, such as invalid input data.
 *
 * @author Serhii Bohdan
 */
@Getter
public class ValidationException extends RuntimeException {

    /**
     * The HTTP status code associated with this exception, indicating the nature of the error to the client.
     */
    private final HttpStatus httpStatus;

    /**
     * Constructs a new {@code ValidationException} with the specified HTTP status code and message.
     *
     * @param httpStatus The HTTP status code associated with the exception.
     * @param message    The message describing the validation error.
     */
    public ValidationException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
