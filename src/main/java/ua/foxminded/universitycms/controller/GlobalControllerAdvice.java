package ua.foxminded.universitycms.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import ua.foxminded.universitycms.exception.CustomException;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Global controller advice class for handling exceptions and configuring data binding.
 * <p>
 * This class provides an {@link ExceptionHandler} for handling {@link CustomException}
 * and an {@link InitBinder} method for automatically trimming strings in data binding.
 *
 * @author Serhii Bohdan
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Handles exceptions of type {@link CustomException} and prepares an error view to be displayed to the user.
     * <p>
     * This method is invoked when a {@code CustomException} is thrown within the application. It sets up a
     * {@link ModelAndView} object pointing to the error page view and includes the exception details
     * as a model attribute for rendering error-specific information on the page.
     *
     * @param ex the {@link CustomException} that was thrown
     * @return a {@link ModelAndView} object pointing to the error page, with the exception details included as a
     * model attribute
     */
    @ExceptionHandler(CustomException.class)
    public ModelAndView handleCustomException(CustomException ex) {
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
