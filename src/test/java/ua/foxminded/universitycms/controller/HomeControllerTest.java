package ua.foxminded.universitycms.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getHomePage_shouldReturnHomePage_whenProvidedHttpMethodGetAndUrlIsValid() throws Exception {
        mockMvc.perform(get("/ui/v1/home"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"));
    }

}
