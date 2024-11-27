package ua.foxminded.universitycms.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.StudentCreationDto;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.dto.UserDto;
import ua.foxminded.universitycms.exception.CustomHttpException;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.model.enumeration.RoleName;
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
     * The URL for redirecting to the page displaying all students.
     */
    private static final String ALL_STUDENTS_REDIRECT_URL = "redirect:/ui/v1/students";

    /**
     * The URL template for redirecting to a specific student's page, with the student ID as a placeholder.
     */
    private static final String PARTICULAR_STUDENTS_REDIRECT_URL = "redirect:/ui/v1/students/%s";

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
        Page<StudentDto> studentsPage = StringUtils.isBlank(keyword)
            ? studentService.getStudentsPage(pageable)
            : studentService.getStudentInPageByEmail(keyword, pageable);

        model.addAttribute(ModelAttributeNames.STUDENTS_ALL_EMAILS_ATTRIBUTE, getStudentEmails(studentService.getAll()))
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
    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    public String getPageWithListOfStudentsNotEnrolledInCourse(Model model, @RequestParam("cid") long courseId,
                                                               @RequestParam(value = "keyword", required = false) String keyword) {
        List<StudentDto> notEnrolledStudents = StringUtils.isBlank(keyword)
            ? studentService.getListOfStudentsNotEnrolledInCourse(courseId)
            : findStudentByEmail(studentService.getListOfStudentsNotEnrolledInCourse(courseId), keyword);

        model.addAttribute(ModelAttributeNames.NOT_ENROLLED_STUDENTS_ATTRIBUTE, notEnrolledStudents)
            .addAttribute(ModelAttributeNames.STUDENT_EMAILS_ATTRIBUTE, getStudentEmails(notEnrolledStudents))
            .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.STUDENTS_NOT_ENROLLED_IN_COURSE;
    }

    /**
     * Retrieves the page displaying information about a specific student.
     * <p>
     * This method handles GET requests to the `/ui/v1/students/{studentId}` endpoint. It retrieves the student
     * with the provided `studentId` using the `studentService`. If the student is found, the method adds the
     * student data to the model for display in the particular student view. If the student is not found, a
     * {@link CustomHttpException} with a not found status is thrown.
     *
     * @param model     the Spring MVC Model object used to store data for the view
     * @param studentId the ID of the student to retrieve
     * @return the logical view name {@code ViewNames.PARTICULAR_STUDENT} representing the student details template
     * @throws CustomHttpException if the student with the provided ID is not found
     */
    @GetMapping("/{studentId}")
    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    public String getPageWithParticularStudent(Model model, @PathVariable("studentId") long studentId) {
        Optional<StudentDto> optional = studentService.getById(studentId);

        if (optional.isPresent()) {
            model.addAttribute(ModelAttributeNames.STUDENT_ATTRIBUTE, optional.get());
            return ViewNames.PARTICULAR_STUDENT;
        }

        throw new CustomHttpException(HttpStatus.NOT_FOUND, "Unable to view student information. Student not found.");
    }

    /**
     * Displays the form for creating a new student.
     * <p>
     * This method is responsible for preparing the model with a new {@link StudentCreationDto} object and
     * a list of all available groups to populate the student creation form. The user must have the
     * {@code STUDENTS_CREATE} authority to access this form.
     *
     * @param model the {@link Model} object used to pass data to the view.
     * @return the name of the view used for the student creation form.
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('STUDENTS_CREATE')")
    public String getCreationForm(Model model) {
        StudentCreationDto student = StudentCreationDto.builder().build();

        model.addAttribute(ModelAttributeNames.STUDENT_ATTRIBUTE, student)
            .addAttribute(ModelAttributeNames.ALL_GROUPS_ATTRIBUTE, studentService.getAllExistingGroups());

        return ViewNames.STUDENT_CREATION_FORM;
    }

    /**
     * Handles the submission of the student creation form and adds a new student to the system.
     * <p>
     * This method is responsible for processing the form submission, validating the input data,
     * and saving a new student using the provided {@link StudentCreationDto}. If validation errors
     * occur, the form is redisplayed with the error messages and available group information.
     * The user must have the {@code STUDENTS_CREATE} authority to perform this operation.
     *
     * @param model         the {@link Model} object used to pass data to the view in case of validation errors.
     * @param student       the {@link StudentCreationDto} containing the student's data submitted from the form.
     * @param bindingResult the {@link BindingResult} containing the result of the validation process.
     * @return the name of the view to navigate to after the student is successfully added, or the form view if there are validation errors.
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('STUDENTS_CREATE')")
    public String performStudentAdding(Model model, @ModelAttribute("student") @Valid StudentCreationDto student,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.ALL_GROUPS_ATTRIBUTE, studentService.getAllExistingGroups());
            return ViewNames.STUDENT_CREATION_FORM;
        }

        studentService.save(student);
        return ALL_STUDENTS_REDIRECT_URL;
    }

    /**
     * Retrieves the form for updating a specific student's information.
     * <p>
     * This method handles GET requests to the `/ui/v1/students/{studentId}/edit` endpoint. It fetches the
     * student data for the given `studentId` using the {@link StudentService}. If the student is found,
     * the method adds the student data and a list of all existing groups to the model, preparing it for
     * display in the update form. If the student is not found, a {@link CustomHttpException} is thrown
     * with a NOT_FOUND status.
     * <p>
     * Users must have the `STUDENTS_UPDATE` authority to access this endpoint.
     *
     * @param model     the Spring MVC Model object used to store data for the view
     * @param studentId the ID of the student to retrieve for editing
     * @return the logical view name {@link ViewNames#STUDENT_UPDATE_FORM} representing the student update form template
     * @throws CustomHttpException if the student with the provided ID is not found
     */
    @GetMapping("/{studentId}/edit")
    @PreAuthorize("hasAuthority('STUDENTS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("studentId") long studentId) {
        Optional<StudentDto> optional = studentService.getById(studentId);

        if (optional.isPresent()) {
            model.addAttribute(ModelAttributeNames.STUDENT_ATTRIBUTE, optional.get())
                .addAttribute(ModelAttributeNames.ALL_GROUPS_ATTRIBUTE, studentService.getAllExistingGroups());
            return ViewNames.STUDENT_UPDATE_FORM;
        }

        throw new CustomHttpException(HttpStatus.NOT_FOUND, "Editing failed. Student not found.");
    }

    /**
     * Handles the update of a student's information.
     * <p>
     * This method processes PUT requests to the `/ui/v1/students/update` endpoint. It validates the
     * provided {@link StudentDto} object and checks for any binding errors. If errors are present,
     * the method adds a list of all existing groups to the model and returns the update form view.
     * If no errors are found, it attempts to update the student using the {@link StudentService}.
     * If the update is successful, it redirects to the page displaying the updated student's information.
     * If the student to be updated is not found, a {@link CustomHttpException} is thrown with the
     * appropriate HTTP status.
     * <p>
     * Users must have the `STUDENTS_UPDATE` authority to access this endpoint.
     *
     * @param model         the Spring MVC Model object used to store data for the view
     * @param student       the {@link StudentDto} object containing the updated student data
     * @param bindingResult the result of the binding operation, containing any validation errors
     * @return the redirect URL for the updated student's page if the update is successful,
     * or the logical view name {@link ViewNames#STUDENT_UPDATE_FORM} if there are binding errors
     * @throws CustomHttpException if the student with the provided ID is not found during the update process
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('STUDENTS_UPDATE')")
    public String performStudentUpdate(Model model, @ModelAttribute("student") @Valid StudentDto student,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.ALL_GROUPS_ATTRIBUTE, studentService.getAllExistingGroups());
            return ViewNames.STUDENT_UPDATE_FORM;
        }

        try {
            studentService.update(student);
            return String.format(PARTICULAR_STUDENTS_REDIRECT_URL, student.getId());
        } catch (EntityNotFoundException e) {
            throw new CustomHttpException(e.getHttpStatus(), "Update failed. Student not found.");
        }
    }

    /**
     * Handles the deletion of a student from the system.
     * <p>
     * This method processes DELETE requests to the `/ui/v1/students/{studentId}/delete` endpoint.
     * It attempts to delete the student with the specified ID using the {@link StudentService}.
     * If the deletion is successful, it redirects to the URL displaying all students.
     * If the student is not found, a {@link CustomHttpException} is thrown with the appropriate
     * HTTP status indicating the failure of the deletion operation.
     * <p>
     * Users must have the `STUDENTS_DELETE` authority to access this endpoint.
     *
     * @param studentId the ID of the student to be deleted
     * @return the redirect URL for displaying all students if the deletion is successful
     * @throws CustomHttpException if the student with the provided ID is not found during the deletion process
     */
    @DeleteMapping("/{studentId}/delete")
    @PreAuthorize("hasAuthority('STUDENTS_DELETE')")
    public String performStudentDeletion(@PathVariable("studentId") long studentId) {
        try {
            studentService.deleteById(studentId);
            return ALL_STUDENTS_REDIRECT_URL;
        } catch (EntityNotFoundException e) {
            throw new CustomHttpException(e.getHttpStatus(), "Deletion failed. Student not found.");
        }
    }

    /**
     * Handles the retrieval of the password change form for a specific student.
     * <p>
     * This method processes GET requests to the `/ui/v1/students/{studentId}/change-pass` endpoint.
     * It initializes a {@link PasswordUpdateRequestDto} object with the specified student's ID and adds it
     * to the model, which will be used in the password update form view.
     * Users must have the `STUDENTS_UPDATE` authority to access this endpoint.
     *
     * @param model     the Spring MVC Model object used to store data for the view
     * @param studentId the ID of the student whose password is to be changed
     * @return the logical view name representing the password update form
     */
    @GetMapping("/{studentId}/change-pass")
    @PreAuthorize("hasAuthority('STUDENTS_UPDATE')")
    public String getChangePasswordForm(Model model, @PathVariable("studentId") long studentId) {
        PasswordUpdateRequestDto passwordUpdateRequest = PasswordUpdateRequestDto.builder()
            .userId(studentId)
            .roleName(RoleName.STUDENT)
            .build();

        model.addAttribute(ModelAttributeNames.PASSWORD_UPDATE_REQUEST_ATTRIBUTE, passwordUpdateRequest);
        return ViewNames.PASSWORD_UPDATE_FORM;
    }

    /**
     * Handles the password update for a student.
     * <p>
     * This method processes the password update request for a specific student. It first validates the input
     * using {@link PasswordUpdateRequestDto}. If validation fails, it returns the password update form.
     * If the input is valid, it proceeds to update the student's password using the {@link StudentService}.
     * If the student cannot be found, it throws a {@link CustomHttpException}.
     *
     * @param passwordUpdateRequest the password update request containing the new password details
     * @param bindingResult         the result of validating the {@link PasswordUpdateRequestDto}
     * @return a redirection URL to the student's page after the update or the password update form if validation fails
     * @throws CustomHttpException if the student cannot be found during the password update process
     */
    @PatchMapping("/update-pass")
    @PreAuthorize("hasAuthority('STUDENTS_UPDATE')")
    public String performPasswordUpdate(@ModelAttribute("passwordUpdateRequest") @Valid PasswordUpdateRequestDto passwordUpdateRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.PASSWORD_UPDATE_FORM;
        }

        try {
            studentService.updateStudentPassword(passwordUpdateRequest);
            return String.format(PARTICULAR_STUDENTS_REDIRECT_URL, passwordUpdateRequest.getUserId());
        } catch (EntityNotFoundException e) {
            throw new CustomHttpException(e.getHttpStatus(), "Password update failed. No student found.");
        }
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
