package ua.foxminded.universitycms.exception;

import org.springframework.http.HttpStatus;

/**
 * A custom exception class representing the scenario where a user cannot be found.
 * <p>
 * This exception is thrown when an operation attempts to retrieve a user by a specific identifier but the user
 * does not exist in the system. It extends {@link CustomException}, allowing the inclusion of an associated HTTP
 * status code and descriptive error message.
 *
 * @author Serhii Bohdan
 */
public class UserNotFoundException extends CustomException {

    /**
     * Constructs a new {@code UserNotFoundException} with the specified HTTP status and error message.
     *
     * @param httpStatus the HTTP status code to be associated with this exception, typically {@link HttpStatus#NOT_FOUND}
     * @param message    a descriptive message explaining the details of the missing user
     */
    public UserNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
