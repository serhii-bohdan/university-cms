package ua.foxminded.universitycms.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import ua.foxminded.universitycms.exception.CustomHttpException;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Global controller advice class for handling exceptions and configuring data binding.
 * <p>
 * This class provides an {@link ExceptionHandler} for handling `CustomHttpException` and an {@link InitBinder} method
 * for automatically trimming strings in data binding.
 *
 * @author Serhii Bohdan
 */
@ControllerAdvice
public class GlobalControllerAdvice {

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
        ModelAndView modelAndView = new ModelAndView(ViewNames.ERROR_PAGE);
        modelAndView.addObject(ModelAttributeNames.EXCEPTION_ATTRIBUTE, ex);
        return modelAndView;
    }

    /**
     * Initializes a {@link WebDataBinder} to automatically trim strings.
     * <p>
     * This method is annotated with {@link InitBinder}, indicating that it should be invoked before
     * data binding occurs. It registers a {@link StringTrimmerEditor} with the binder, which will automatically
     * trim leading and trailing whitespace from String values.
     *
     * @param binder the {@link WebDataBinder} to initialize
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(false);
        binder.registerCustomEditor(String.class, stringTrimmer);
    }

}
