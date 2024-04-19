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
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.service.StudentService;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying students.
 * It maps GET requests to the {@code /ui/v1/students} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequestMapping("/ui/v1/students")
public class StudentController {

    /**
     * The {@link StudentService} used to interact with student data.
     */
    private final StudentService studentService;

    /**
     * Constructs a new {@code StudentController} instance with the given {@link StudentService}.
     *
     * @param studentService the {@link StudentService} to use for student-related operations
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

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
    public String getPageWithStudents(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                      @PageableDefault Pageable pageable) {
        Page<StudentDto> studentsPage = new PageImpl<>(new ArrayList<>());
        boolean hasError = false;

        try {
            if (Objects.nonNull(keyword) && !keyword.isBlank()) {
                studentsPage = studentService.getStudentInPageByName(keyword, pageable);
            } else {
                studentsPage = studentService.getStudentsPage(pageable);
            }
        } catch (ServiceException e) {
            hasError = true;
        }

        model.addAttribute("allNamesOfStudents", studentService.getAllNamesOfStudents())
            .addAttribute("students", studentsPage.getContent())
            .addAttribute("page", pageable.getPageNumber())
            .addAttribute("totalItems", studentsPage.getTotalElements())
            .addAttribute("totalPages", studentsPage.getTotalPages())
            .addAttribute("size", pageable.getPageSize())
            .addAttribute("keyword", keyword)
            .addAttribute("hasError", hasError);

        return "students/all-students";
    }

}
