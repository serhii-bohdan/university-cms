package ua.foxminded.universitycms.controller;

import java.util.Collection;
import java.util.List;
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
import ua.foxminded.universitycms.dto.UserDto;
import ua.foxminded.universitycms.exception.CustomHttpException;
import ua.foxminded.universitycms.exception.InvalidFullNameFormatException;
import ua.foxminded.universitycms.service.StudentService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

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

        model.addAttribute(ModelAttributeNames.STUDENTS_ALL_NAMES_ATTRIBUTE, studentService.getAllNamesOfStudents())
            .addAttribute(ModelAttributeNames.STUDENTS_ATTRIBUTE, studentsPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, studentsPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, studentsPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_STUDENTS_PAGE;
    }

    /**
     * Retrieves a list of students not enrolled in a specified course.
     * <p>
     * This method handles GET requests to the `/not-enrolled` endpoint. It retrieves a list of students not currently
     * enrolled in the course with the provided `courseId` using the `studentService`. Students can be optionally filtered
     * based on a search keyword for email addresses. The list of students and any extracted student emails are added to
     * the model for display.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param courseId the ID of the course
     * @param keyword  an optional search keyword for filtering students by email (can be blank)
     * @return the logical view name "students/not-enrolled-in-course" representing the list of not enrolled students
     * @throws CustomHttpException if an unexpected error occurs while retrieving student data
     */
    @GetMapping("/not-enrolled")
    @PreAuthorize("hasAuthority('COURSES_READ')")
    public String getPageWithListOfStudentsNotEnrolledInCourse(Model model, @RequestParam("cid") long courseId,
                                                               @RequestParam(value = "keyword", required = false) String keyword) {
        List<StudentDto> notEnrolledStudents = StringUtils.isBlank(keyword)
            ? studentService.getListOfStudentsNotEnrolledInCourse(courseId)
            : findStudentByEmail(studentService.getListOfStudentsNotEnrolledInCourse(courseId), keyword.strip());

        model.addAttribute(ModelAttributeNames.NOT_ENROLLED_STUDENTS_ATTRIBUTE, notEnrolledStudents)
            .addAttribute(ModelAttributeNames.STUDENT_EMAILS_ATTRIBUTE, getStudentEmails(notEnrolledStudents))
            .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.STUDENTS_NOT_ENROLLED_IN_COURSE;
    }

    private List<StudentDto> findStudentByEmail(Collection<StudentDto> students, String email) {
        return students.stream()
            .filter(s -> s.getEmail().equals(email))
            .toList();
    }

    private List<String> getStudentEmails(Collection<StudentDto> students) {
        return students.stream()
            .map(UserDto::getEmail)
            .toList();
    }

}
