package ua.foxminded.universitycms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * A custom exception class representing an HTTP-related error within the application.
 * <p>
 * This exception is designed to be thrown when an error occurs during the processing of an HTTP request or response.
 * It allows for capturing both a detailed error message and an associated {@link HttpStatus} code to provide
 * more context and facilitate appropriate error handling and communication to the client.
 *
 * @author Serhii Bohdan
 */
@Getter
public class CustomHttpException extends RuntimeException {

    /**
     * The HTTP status code associated with this exception, indicating the nature of the error to the client.
     */
    private final HttpStatus httpStatus;

    /**
     * Constructs a new `CustomHttpException` with the specified HTTP status code and error message.
     *
     * @param httpStatus the HTTP status code to associate with this exception
     * @param message    a detailed message describing the specific error that occurred
     */
    public CustomHttpException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
