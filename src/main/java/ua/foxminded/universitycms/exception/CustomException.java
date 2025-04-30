package ua.foxminded.universitycms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Abstract base class for custom exceptions in the university management system.
 * <p>
 * This class extends {@link RuntimeException} to provide a foundation for application-specific
 * exceptions, associating each with an HTTP status code to indicate the error type to clients.
 * Subclasses can extend this class to encapsulate additional domain-specific details or behaviors,
 * enabling consistent error handling across the application.
 *
 * @author Serhii Bohdan
 * @see RuntimeException
 * @see HttpStatus
 */
@Getter
public abstract class CustomException extends RuntimeException {

    /**
     * The HTTP status code associated with this exception.
     * <p>
     * This field specifies the HTTP status to be returned to the client when the exception
     * is thrown, providing a standardized indication of the error type.
     */
    protected final HttpStatus httpStatus;

    /**
     * Constructs a new {@code CustomException} with the specified HTTP status and message.
     * <p>
     * Initializes the exception with a detail message passed to the superclass ({@link RuntimeException})
     * and an {@link HttpStatus} value to categorize the error for client responses.
     *
     * @param httpStatus the HTTP status code indicating the error type
     * @param message    the detail message providing context about the exception cause
     */
    protected CustomException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
