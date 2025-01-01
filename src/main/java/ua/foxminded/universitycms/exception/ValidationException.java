package ua.foxminded.universitycms.exception;

import org.springframework.http.HttpStatus;

/**
 * A custom exception class representing validation errors in the application.
 * <p>
 * This exception is typically thrown when input data fails to meet the required constraints
 * or business rules during validation processes. It extends {@link CustomException}, allowing
 * an HTTP status code and descriptive message to be included for better error handling.
 *
 * @author Serhii Bohdan
 */
public class ValidationException extends CustomException {

    /**
     * Constructs a new {@code ValidationException} with the specified HTTP status and error message.
     *
     * @param httpStatus the HTTP status code associated with this exception, typically {@link HttpStatus#BAD_REQUEST}
     * @param message    a descriptive message providing details about the validation error
     */
    public ValidationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
