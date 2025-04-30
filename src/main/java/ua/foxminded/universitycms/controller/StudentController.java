package ua.foxminded.universitycms.controller;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.StudentCreationDto;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.service.StudentService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Spring MVC Controller for handling student-related requests under the {@code /ui/v1/students} path.
 * Manages operations like displaying student lists, creating, updating, and deleting students, and
 * updating passwords. Uses {@link StudentService} for business logic. Annotated with {@code @Controller}
 * for MVC handling and {@code @RequiredArgsConstructor} for dependency injection.
 *
 * @author Serhii Bohdan
 * @see StudentService
 * @see ModelAttributeNames
 * @see ViewNames
 */
@Controller
@RequiredArgsConstructor
@RequestMapping({"/ui/v1/students"})
public class StudentController {

    /**
     * URL for redirecting to the all students page after operations.
     */
    private static final String ALL_STUDENTS_REDIRECT_URL = "redirect:/ui/v1/students";

    /**
     * URL template for redirecting to a specific student's page, with a placeholder for student ID.
     */
    private static final String PARTICULAR_STUDENTS_REDIRECT_URL = "redirect:/ui/v1/students/%s";

    /**
     * Service for interacting with student data and performing business logic operations.
     */
    private final StudentService studentService;

    /**
     * Displays a paginated list of students, optionally filtered by keyword.
     * Handles GET requests to {@code /ui/v1/students}. Retrieves students via
     * {@link StudentService#findStudents} and adds pagination data and emails to the model.
     * Requires {@code STUDENTS_READ} authority.
     *
     * @param model    the {@link Model} to store view data
     * @param keyword  optional keyword to filter students by email; may be blank
     * @param pageable pagination info from {@link PageableDefault}
     * @return view name {@link ViewNames#ALL_STUDENTS_PAGE} for the student list
     */
    @GetMapping
    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    public String getPageWithStudents(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                      @PageableDefault Pageable pageable) {
        Page<StudentDto> studentsPage = studentService.findStudents(pageable, keyword);

        model.addAttribute(ModelAttributeNames.STUDENTS_ALL_EMAILS_ATTRIBUTE, studentService.extractStudentEmails(studentService.getAll()))
            .addAttribute(ModelAttributeNames.STUDENTS_ATTRIBUTE, studentsPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, studentsPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, studentsPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_STUDENTS_PAGE;
    }

    /**
     * Displays students not enrolled in a specific course, optionally filtered by keyword.
     * Handles GET requests to {@code /ui/v1/students/not-enrolled}. Retrieves unenrolled students via
     * {@link StudentService#getUnEnrolledStudents} and adds them to the model. Requires
     * {@code STUDENTS_READ}.
     *
     * @param model    the {@link Model} to store view data
     * @param courseId the ID of the course to check enrollment against
     * @param keyword  optional keyword to filter students by email; may be blank
     * @return view name {@link ViewNames#STUDENTS_NOT_ENROLLED_IN_COURSE} for the list
     */
    @GetMapping("/not-enrolled")
    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    public String getPageWithListOfStudentsNotEnrolledInCourse(Model model, @RequestParam("cid") long courseId,
                                                               @RequestParam(value = "keyword", required = false) String keyword) {
        List<StudentDto> unEnrolledStudents = studentService.getUnEnrolledStudents(courseId, keyword);

        model.addAttribute(ModelAttributeNames.NOT_ENROLLED_STUDENTS_ATTRIBUTE, unEnrolledStudents)
            .addAttribute(ModelAttributeNames.STUDENT_EMAILS_ATTRIBUTE, studentService.extractStudentEmails(unEnrolledStudents))
            .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.STUDENTS_NOT_ENROLLED_IN_COURSE;
    }

    /**
     * Displays details of a specific student.
     * Handles GET requests to {@code /ui/v1/students/{studentId}}. Retrieves student data via
     * {@link StudentService#getById} and adds it to the model. Requires {@code STUDENTS_READ}.
     *
     * @param model     the {@link Model} to store view data
     * @param studentId the ID of the student to display
     * @return view name {@link ViewNames#PARTICULAR_STUDENT} for student details
     */
    @GetMapping("/{studentId}")
    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    public String getPageWithParticularStudent(Model model, @PathVariable("studentId") long studentId) {
        model.addAttribute(ModelAttributeNames.STUDENT_ATTRIBUTE, studentService.getById(studentId));
        return ViewNames.PARTICULAR_STUDENT;
    }

    /**
     * Displays the form for creating a new student.
     * Handles GET requests to {@code /ui/v1/students/new}. Prepares a {@link StudentCreationDto} and
     * group list for the form. Requires {@code STUDENTS_CREATE} authority.
     *
     * @param model the {@link Model} to store form data
     * @return view name {@link ViewNames#STUDENT_CREATION_FORM} for the creation form
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('STUDENTS_CREATE')")
    public String getCreationForm(Model model) {
        StudentCreationDto student = StudentCreationDto.builder().build();
        model.addAttribute(ModelAttributeNames.STUDENT_ATTRIBUTE, student)
            .addAttribute(ModelAttributeNames.ALL_GROUPS_ATTRIBUTE, studentService.getAllExistingGroups())
            .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
        return ViewNames.STUDENT_CREATION_FORM;
    }

    /**
     * Processes the submission of the student creation form.
     * Handles POST requests to {@code /ui/v1/students/add}. Validates {@link StudentCreationDto} and
     * saves the student via {@link StudentService#save}. Returns the form on errors. Requires
     * {@code STUDENTS_CREATE}.
     *
     * @param model         the {@link Model} for error handling
     * @param student       the {@link StudentCreationDto} with form data
     * @param bindingResult validation results for the DTO
     * @return redirect to {@link #ALL_STUDENTS_REDIRECT_URL} or form view on errors
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('STUDENTS_CREATE')")
    public String performStudentAdding(Model model, @ModelAttribute("student") @Valid StudentCreationDto student,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.ALL_GROUPS_ATTRIBUTE, studentService.getAllExistingGroups())
                .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
            return ViewNames.STUDENT_CREATION_FORM;
        }

        studentService.save(student);
        return ALL_STUDENTS_REDIRECT_URL;
    }

    /**
     * Displays the form for updating a student's information.
     * Handles GET requests to {@code /ui/v1/students/{studentId}/edit}. Retrieves student data and
     * groups via {@link StudentService} for the form. Requires {@code STUDENTS_UPDATE}.
     *
     * @param model     the {@link Model} to store form data
     * @param studentId the ID of the student to update
     * @return view name {@link ViewNames#STUDENT_UPDATE_FORM} for the update form
     */
    @GetMapping("/{studentId}/edit")
    @PreAuthorize("hasAuthority('STUDENTS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("studentId") long studentId) {
        model.addAttribute(ModelAttributeNames.STUDENT_ATTRIBUTE, studentService.getById(studentId))
            .addAttribute(ModelAttributeNames.ALL_GROUPS_ATTRIBUTE, studentService.getAllExistingGroups())
            .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
        return ViewNames.STUDENT_UPDATE_FORM;
    }

    /**
     * Processes the update of a student's information.
     * Handles PUT requests to {@code /ui/v1/students/update}. Validates {@link StudentDto} and updates
     * via {@link StudentService#update}. Returns form on errors. Requires {@code STUDENTS_UPDATE}.
     *
     * @param model         the {@link Model} for error handling
     * @param student       the {@link StudentDto} with updated data
     * @param bindingResult validation results for the DTO
     * @return redirect to student page or form view on errors
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('STUDENTS_UPDATE')")
    public String performStudentUpdate(Model model, @ModelAttribute("student") @Valid StudentDto student,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.ALL_GROUPS_ATTRIBUTE, studentService.getAllExistingGroups())
                .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
            return ViewNames.STUDENT_UPDATE_FORM;
        }

        studentService.update(student);
        return PARTICULAR_STUDENTS_REDIRECT_URL.formatted(student.getId());
    }

    /**
     * Deletes a student from the system.
     * Handles DELETE requests to {@code /ui/v1/students/{studentId}/delete}. Deletes student via
     * {@link StudentService#deleteById} and redirects. Requires {@code STUDENTS_DELETE}.
     *
     * @param studentId the ID of the student to delete
     * @return redirect to {@link #ALL_STUDENTS_REDIRECT_URL}
     */
    @DeleteMapping("/{studentId}/delete")
    @PreAuthorize("hasAuthority('STUDENTS_DELETE')")
    public String performStudentDeletion(@PathVariable("studentId") long studentId) {
        studentService.deleteById(studentId);
        return ALL_STUDENTS_REDIRECT_URL;
    }

    /**
     * Displays the password change form for a student.
     * Handles GET requests to {@code /ui/v1/students/{studentId}/change-pass}. Prepares a
     * {@link PasswordUpdateRequestDto} for the form. Requires {@code STUDENTS_UPDATE}.
     *
     * @param model     the {@link Model} to store form data
     * @param studentId the ID of the student whose password will change
     * @return view name {@link ViewNames#PASSWORD_UPDATE_FORM} for the form
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
     * Updates a student's password.
     * Handles PATCH requests to {@code /ui/v1/students/update-pass}. Validates
     * {@link PasswordUpdateRequestDto} and updates via {@link StudentService#updateStudentPassword}.
     * Requires {@code STUDENTS_UPDATE}.
     *
     * @param passwordUpdateRequest the DTO with new password details
     * @param bindingResult         validation results for the DTO
     * @return redirect to student page or form view on errors
     */
    @PatchMapping("/update-pass")
    @PreAuthorize("hasAuthority('STUDENTS_UPDATE')")
    public String performPasswordUpdate(@ModelAttribute("passwordUpdateRequest") @Valid PasswordUpdateRequestDto passwordUpdateRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.PASSWORD_UPDATE_FORM;
        }

        studentService.updateStudentPassword(passwordUpdateRequest);
        return PARTICULAR_STUDENTS_REDIRECT_URL.formatted(passwordUpdateRequest.getUserId());
    }

}
