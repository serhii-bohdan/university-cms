package ua.foxminded.universitycms.exception;

import org.springframework.http.HttpStatus;

/**
 * A custom exception class representing the scenario where an entity cannot be found.
 * <p>
 * This exception is typically thrown when an operation requires a specific entity by its identifier
 * or attributes, but the entity does not exist in the system. It extends {@link CustomException}
 * to include an HTTP status code.
 *
 * @author Serhii Bohdan
 */
public class EntityNotFoundException extends CustomException {

    /**
     * Constructs a new {@code EntityNotFoundException} with the specified HTTP status and error message.
     *
     * @param httpStatus the HTTP status code to be associated with this exception, typically {@link HttpStatus#NOT_FOUND}
     * @param message    a descriptive message about the entity that was not found
     */
    public EntityNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
