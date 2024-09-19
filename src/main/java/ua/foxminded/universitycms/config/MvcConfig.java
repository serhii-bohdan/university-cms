package ua.foxminded.universitycms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Spring MVC customization.
 * <p>
 * This class implements {@link WebMvcConfigurer} to add custom view controllers
 * for specific URL patterns.
 *
 * @author Serhii Bohdan
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Adds custom view controllers for handling specific URL patterns.
     *
     * <p>This method is called by the Spring MVC framework to allow customization
     * of the default view controller registry. The following mappings are added:
     *
     * <ul>
     *   <li>"/ui/v1/home" is mapped to the view named "home"</li>
     *   <li>"/login-form" is mapped to the view named "/security/login"</li>
     *   <li>"/logout-confirmation" is mapped to the view named "/security/logout-confirm"</li>
     * </ul>
     *
     * @param registry The view controller registry to which mappings are added.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/ui/v1/home").setViewName("home");
        registry.addViewController("/login-form").setViewName("/security/login");
        registry.addViewController("/logout-confirmation").setViewName("/security/logout-confirm");
    }

}
