package ua.foxminded.universitycms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested entity cannot be found in the underlying data store.
 * <p>
 * This exception typically occurs when trying to retrieve or modify an entity using an ID or
 * other identifier, but the corresponding entity doesn't exist in the database or other persistent storage.
 *
 * @author Serhii Bohdan
 */
@Getter
public class EntityNotFoundException extends RuntimeException {

    /**
     * The HTTP status code associated with this exception.
     */
    private final HttpStatus httpStatus;

    /**
     * Constructs a new {@code EntityNotFoundException} with the specified HTTP status and error message.
     *
     * @param httpStatus the HTTP status code to be associated with this exception
     * @param message    a descriptive message about the entity that was not found
     */
    public EntityNotFoundException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
