package ua.foxminded.universitycms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This Spring Boot Web Controller serves the home page of the application.
 * It maps all HTTP requests to the root path {@code /ui/v1/home}.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequestMapping("/ui/v1/home")
public class HomeController {

    /**
     * Renders the home page of the application.
     * This method handles GET requests to the root path of the controller mapping (`/ui/v1/home`).
     *
     * @return the logical name of the view template ("home")
     */
    @GetMapping
    public String getHomePage() {
        return "home";
    }

}
