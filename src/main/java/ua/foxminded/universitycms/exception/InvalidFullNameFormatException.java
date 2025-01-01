package ua.foxminded.universitycms.exception;

import org.springframework.http.HttpStatus;

/**
 * A custom exception class representing invalid formatting of a full name.
 * <p>
 * This exception is typically thrown when a user's full name fails validation due to incorrect formatting,
 * such as missing required components or containing invalid characters.
 * It extends {@link CustomException}.
 *
 * @author Serhii Bohdan
 */
public class InvalidFullNameFormatException extends CustomException {

    /**
     * Constructs a new {@code InvalidFullNameFormatException} with the specified HTTP status and error message.
     *
     * @param httpStatus the HTTP status code to be associated with this exception, typically {@link HttpStatus#BAD_REQUEST}
     * @param message    a descriptive message explaining the formatting error
     */
    public InvalidFullNameFormatException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
