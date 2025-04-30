package ua.foxminded.universitycms.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception representing the case where a requested entity cannot be found in the university management system.
 * <p>
 * This exception extends {@link CustomException} and is thrown when an operation attempts to access or manipulate
 * an entity (e.g., by its identifier or attributes) that does not exist in the system. It associates an HTTP status
 * code, typically {@link HttpStatus#NOT_FOUND}, to indicate the absence of the entity to clients.
 *
 * @author Serhii Bohdan
 * @see CustomException
 * @see HttpStatus
 */
public class EntityNotFoundException extends CustomException {

    /**
     * Constructs a new {@code EntityNotFoundException} with the specified HTTP status and detail message.
     * <p>
     * Initializes the exception with an {@link HttpStatus} value and a message describing the entity that could
     * not be found, passing these to the superclass constructor.
     *
     * @param httpStatus the HTTP status code associated with this exception, typically {@link HttpStatus#NOT_FOUND}
     * @param message    a detailed message explaining why the entity was not found
     */
    public EntityNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
