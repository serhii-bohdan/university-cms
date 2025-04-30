package ua.foxminded.universitycms.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception representing the case where a user cannot be found in the university management system.
 * <p>
 * This exception extends {@link CustomException} and is thrown when an operation attempts to retrieve a user
 * by a specific identifier or other attributes, but no matching user exists in the system. It associates an HTTP
 * status code, typically {@link HttpStatus#NOT_FOUND}, to indicate the absence of the user to clients.
 *
 * @author Serhii Bohdan
 * @see CustomException
 * @see HttpStatus
 */
public class UserNotFoundException extends CustomException {

    /**
     * Constructs a new {@code UserNotFoundException} with the specified HTTP status and detail message.
     * <p>
     * Initializes the exception with an {@link HttpStatus} value and a message describing the missing user,
     * passing these to the superclass constructor.
     *
     * @param httpStatus the HTTP status code associated with this exception, typically {@link HttpStatus#NOT_FOUND}
     * @param message    a detailed message explaining why the user was not found
     */
    public UserNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
