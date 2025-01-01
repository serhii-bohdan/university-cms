package ua.foxminded.universitycms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * A base class for custom exceptions in the application.
 * <p>
 * This abstract class extends {@link RuntimeException} and provides a mechanism to associate
 * an HTTP status code with the exception. Subclasses can use this to signal specific HTTP
 * error codes to the client when the exception is thrown.
 * <p>
 * Custom exceptions that extend this class can include additional information or behaviors
 * relevant to the application's domain logic.
 *
 * @author Serhii Bohdan
 */
@Getter
public abstract class CustomException extends RuntimeException {

    /**
     * The HTTP status code associated with this exception, indicating the nature of the error to the client.
     */
    protected final HttpStatus httpStatus;

    /**
     * Constructs a new {@code CustomException} with the specified HTTP status and detail message.
     *
     * @param httpStatus the HTTP status code associated with this exception, indicating the type of error
     * @param message    the detail message, providing additional context about the exception
     */
    protected CustomException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
