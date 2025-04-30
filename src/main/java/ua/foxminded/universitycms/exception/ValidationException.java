package ua.foxminded.universitycms.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception representing validation errors in the university management system.
 * <p>
 * This exception extends {@link CustomException} and is thrown when input data fails to satisfy
 * required constraints, business rules, or validation criteria during processing. It associates an
 * HTTP status code, typically {@link HttpStatus#BAD_REQUEST}, to indicate invalid input to clients,
 * along with a descriptive message for detailed error reporting.
 *
 * @author Serhii Bohdan
 * @see CustomException
 * @see HttpStatus
 */
public class ValidationException extends CustomException {

    /**
     * Constructs a new {@code ValidationException} with the specified HTTP status and detail message.
     * <p>
     * Initializes the exception with an {@link HttpStatus} value and a message explaining the
     * validation failure, passing these to the superclass constructor.
     *
     * @param httpStatus the HTTP status code associated with this exception, typically
     *                   {@link HttpStatus#BAD_REQUEST}
     * @param message    a detailed message describing the validation error (e.g., "Email must not be empty")
     */
    public ValidationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
