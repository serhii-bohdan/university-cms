package ua.foxminded.universitycms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Configuration class for customizing Spring MVC settings in the university management system.
 * <p>
 * This class implements {@link WebMvcConfigurer} to provide custom configurations for Spring MVC,
 * specifically by defining view controllers that map URL patterns to view names without requiring
 * explicit controller logic. The {@code @Configuration} annotation marks this class as a source of
 * bean definitions for the Spring application context.
 *
 * @author Serhii Bohdan
 * @see WebMvcConfigurer
 * @see ViewControllerRegistry
 * @see ViewNames
 * @see org.springframework.context.annotation.Configuration
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Configures view controllers to map specific URL patterns to predefined view names.
     * <p>
     * This method overrides the default behavior of Spring MVC by registering view controllers in the
     * provided {@link ViewControllerRegistry}. It establishes direct mappings between URLs and views,
     * bypassing traditional controller logic. The following mappings are defined:
     * <ul>
     *   <li>{@code /} maps to the view name {@link ViewNames#HOME_PAGE}</li>
     *   <li>{@code /ui/v1/home} maps to the view name {@link ViewNames#HOME_PAGE}</li>
     *   <li>{@code /login-form} maps to the view name {@link ViewNames#LOGIN_PAGE}</li>
     * </ul>
     *
     * @param registry the {@link ViewControllerRegistry} used to register view controllers
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName(ViewNames.HOME_PAGE);
        registry.addViewController("/ui/v1/home").setViewName(ViewNames.HOME_PAGE);
        registry.addViewController("/login-form").setViewName(ViewNames.LOGIN_PAGE);
    }

}
