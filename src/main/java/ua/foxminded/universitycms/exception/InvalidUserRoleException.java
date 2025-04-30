package ua.foxminded.universitycms.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception representing the case where a user's role is invalid or cannot be processed in the
 * university management system.
 * <p>
 * This exception extends {@link CustomException} and is thrown when an operation encounters an issue
 * related to a user's role, such as an unrecognized, unauthorized, or misconfigured role. It associates
 * an HTTP status code, typically {@link HttpStatus#BAD_REQUEST} or {@link HttpStatus#FORBIDDEN}, to
 * indicate the nature of the error to clients.
 *
 * @author Serhii Bohdan
 * @see CustomException
 * @see HttpStatus
 */
public class InvalidUserRoleException extends CustomException {

    /**
     * Constructs a new {@code InvalidUserRoleException} with the specified HTTP status and detail message.
     * <p>
     * Initializes the exception with an {@link HttpStatus} value and a message describing the role-related issue,
     * passing these to the superclass constructor.
     *
     * @param httpStatus the HTTP status code associated with this exception, such as {@link HttpStatus#BAD_REQUEST}
     *                   or {@link HttpStatus#FORBIDDEN}
     * @param message    a detailed message explaining the reason for the invalid role
     */
    public InvalidUserRoleException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
