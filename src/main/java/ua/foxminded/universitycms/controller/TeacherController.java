package ua.foxminded.universitycms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.service.TeacherService;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying teachers.
 * It maps GET requests to the {@code /ui/v1/teachers} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequestMapping("/ui/v1/teachers")
public class TeacherController {

    /**
     * The {@link TeacherService} used to interact with teacher data.
     */
    private final TeacherService teacherService;

    /**
     * Constructs a new {@code TeacherController} instance with the given {@link TeacherService}.
     *
     * @param teacherService the {@link TeacherService} to use for teacher-related operations
     */
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * Retrieves a page of teacher data for display and populates the model with necessary attributes.
     * <p>
     * This method handles GET requests to the endpoint responsible for displaying a paginated list of teachers.
     * It utilizes the `teacherService` to retrieve teacher data based on a provided keyword (optional)
     * and pagination information.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param keyword  an optional search keyword for filtering teachers by name (can be blank)
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return the logical view name "teachers/all-teachers" representing the teacher list template
     */
    @GetMapping
    public String getPageWithTeachers(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                      @PageableDefault Pageable pageable) {
        Page<TeacherDto> teachersPage = new PageImpl<>(new ArrayList<>());
        boolean hasError = false;

        try {
            if (Objects.nonNull(keyword) && !keyword.isBlank()) {
                teachersPage = teacherService.getTeacherInPageByName(keyword, pageable);
            } else {
                teachersPage = teacherService.getTeachersPage(pageable);
            }
        } catch (ServiceException e) {
            hasError = true;
        }

        model.addAttribute("allNamesOfTeachers", teacherService.getAllNamesOfTeachers())
            .addAttribute("teachers", teachersPage.getContent())
            .addAttribute("page", pageable.getPageNumber())
            .addAttribute("totalItems", teachersPage.getTotalElements())
            .addAttribute("totalPages", teachersPage.getTotalPages())
            .addAttribute("size", pageable.getPageSize())
            .addAttribute("keyword", keyword)
            .addAttribute("hasError", hasError);

        return "teachers/all-teachers";
    }

}
