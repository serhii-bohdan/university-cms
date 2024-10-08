package ua.foxminded.universitycms.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.exception.CustomHttpException;
import ua.foxminded.universitycms.exception.InvalidFullNameFormatException;
import ua.foxminded.universitycms.service.TeacherService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying teachers.
 * It maps GET requests to the {@code /ui/v1/teachers} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/teachers")
public class TeacherController {

    /**
     * The {@link TeacherService} used to interact with teacher data.
     */
    private final TeacherService teacherService;

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
    @PreAuthorize("hasAuthority('TEACHERS_READ')")
    public String getPageWithTeachers(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                      @PageableDefault Pageable pageable) {
        Page<TeacherDto> teachersPage;

        try {
            teachersPage = StringUtils.isBlank(keyword)
                ? teacherService.getTeachersPage(pageable)
                : teacherService.getTeacherInPageByName(keyword, pageable);
        } catch (InvalidFullNameFormatException e) {
            throw new CustomHttpException(e.getHttpStatus(), "Invalid full name format");
        }

        model.addAttribute(ModelAttributeNames.TEACHERS_ALL_NAMES_ATTRIBUTE, teacherService.getAllNamesOfTeachers())
            .addAttribute(ModelAttributeNames.TEACHERS_ATTRIBUTE, teachersPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, teachersPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, teachersPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_TEACHERS_PAGE;
    }

}
