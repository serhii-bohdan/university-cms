package ua.foxminded.universitycms.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ua.foxminded.universitycms.exception.CustomHttpException;

/**
 * A global exception handler for handling {@link CustomHttpException} exceptions throughout the application.
 * <p>
 * This class is annotated with {@link ControllerAdvice}, allowing it to intercept and handle exceptions
 * thrown by any controller in the application.
 *
 * @author Serhii Bohdan
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions of type {@link CustomHttpException}.
     * <p>
     * This method is annotated with {@link ExceptionHandler}, indicating that it should be invoked when
     * a `CustomHttpException` is thrown. It creates a {@link ModelAndView} object with the "error" view name,
     * adds the exception's message and HTTP status code to the model, and returns the ModelAndView for rendering.
     *
     * @param ex the {@link CustomHttpException} that was thrown
     * @return a {@link ModelAndView} object representing the error view, populated with the exception details
     */
    @ExceptionHandler(CustomHttpException.class)
    public ModelAndView handleCustomHttpException(CustomHttpException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("httpStatus", ex.getHttpStatus());
        return modelAndView;
    }

}
