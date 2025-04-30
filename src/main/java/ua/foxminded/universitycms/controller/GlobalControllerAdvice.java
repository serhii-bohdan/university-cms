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
 * Global controller advice for handling exceptions and configuring data binding across all controllers.
 * Provides an {@link ExceptionHandler} for {@link CustomException} and an {@link InitBinder} for
 * trimming strings during data binding. Annotated with {@code @ControllerAdvice}.
 *
 * @author Serhii Bohdan
 * @see CustomException
 * @see ModelAttributeNames
 * @see ViewNames
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Handles {@link CustomException} by rendering an error page with exception details.
     * Invoked when a {@code CustomException} occurs, creating a {@link ModelAndView} with the error
     * view and exception data.
     *
     * @param ex the {@link CustomException} thrown during execution
     * @return a {@link ModelAndView} with the error page and exception as a model attribute
     */
    @ExceptionHandler({CustomException.class})
    public ModelAndView handleCustomException(CustomException ex) {
        ModelAndView modelAndView = new ModelAndView(ViewNames.CUSTOM_ERROR_PAGE);
        modelAndView.addObject(ModelAttributeNames.EXCEPTION_ATTRIBUTE, ex);
        return modelAndView;
    }

    /**
     * Configures data binding to trim strings automatically.
     * Registers a {@link StringTrimmerEditor} with the {@link WebDataBinder} to remove leading and
     * trailing whitespace from String inputs. Invoked before binding via {@link InitBinder}.
     *
     * @param binder the {@link WebDataBinder} to configure
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(false);
        binder.registerCustomEditor(String.class, stringTrimmer);
    }

}
