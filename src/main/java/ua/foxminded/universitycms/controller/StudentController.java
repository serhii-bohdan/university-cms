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
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.exception.CustomHttpException;
import ua.foxminded.universitycms.exception.InvalidFullNameFormatException;
import ua.foxminded.universitycms.service.StudentService;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying students.
 * It maps GET requests to the {@code /ui/v1/students} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/students")
public class StudentController {

    /**
     * The {@link StudentService} used to interact with student data.
     */
    private final StudentService studentService;

    /**
     * Retrieves a page of student data for display and populates the model with necessary attributes.
     * <p>
     * This method handles GET requests to the endpoint responsible for displaying a paginated list of students.
     * It utilizes the `studentService` to retrieve student data based on a provided keyword (optional)
     * and pagination information.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param keyword  an optional search keyword for filtering students by name (can be blank)
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return the logical view name "students/all-students" representing the student list template
     */
    @GetMapping
    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    public String getPageWithStudents(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                      @PageableDefault Pageable pageable) {
        Page<StudentDto> studentsPage;

        try {
            studentsPage = StringUtils.isBlank(keyword)
                ? studentService.getStudentsPage(pageable)
                : studentService.getStudentInPageByName(keyword, pageable);
        } catch (InvalidFullNameFormatException e) {
            throw new CustomHttpException(e.getHttpStatus(), "Invalid full name format");
        }

        model.addAttribute("allNamesOfStudents", studentService.getAllNamesOfStudents())
            .addAttribute("students", studentsPage.getContent())
            .addAttribute("page", pageable.getPageNumber())
            .addAttribute("totalItems", studentsPage.getTotalElements())
            .addAttribute("totalPages", studentsPage.getTotalPages())
            .addAttribute("size", pageable.getPageSize())
            .addAttribute("keyword", keyword);

        return "students/all-students";
    }

}
