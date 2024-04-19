package ua.foxminded.universitycms.exception;

import java.io.Serial;

/**
 * The {@code ServiceException} class represents a general exception that
 * occurs within application services. It extends {@link RuntimeException}
 * to indicate that it's an unchecked exception. This makes it suitable for
 * use in service-layer methods as a way to signal unexpected errors without
 * disrupting method signatures.
 *
 * @author Serhii Bohdan
 */
public class ServiceException extends RuntimeException {

    /**
     * A version number used for serialization. This field is required when a class is designed to be
     * serialized and deserialized.
     */
    @Serial
    private static final long serialVersionUID = -353917605081670150L;

    /**
     * Creates a {@code ServiceException} instance with no error message.
     */
    public ServiceException() {
        super();
    }

    /**
     * Creates a {@code ServiceException} instance with a specific error message.
     *
     * @param message the error message describing the reason for the exception
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Creates a {@code ServiceException} instance with a specific error message and a cause exception.
     *
     * @param message the error message describing the reason for the exception
     * @param cause   the cause exception that triggered the {@code ServiceException}
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a {@code ServiceException} instance with a cause exception.
     *
     * @param cause the cause exception that triggered the {@code ServiceException}
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

}
