package ua.foxminded.universitycms.controller;

import java.util.Collection;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.TeacherCreationDto;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.dto.UserDto;
import ua.foxminded.universitycms.model.enumeration.RoleName;
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
     * Redirect URL for accessing the list of all teachers.
     */
    private static final String ALL_TEACHERS_REDIRECT_URL = "redirect:/ui/v1/teachers";

    /**
     * Redirect URL for accessing a specific teacher's details.
     */
    private static final String PARTICULAR_TEACHER_REDIRECT_URL = "redirect:/ui/v1/teachers/%s";

    /**
     * The {@link TeacherService} used to interact with teacher data.
     */
    private final TeacherService teacherService;

    /**
     * Retrieves a page of teacher data for display and populates the model with necessary attributes.
     * <p>
     * This method handles GET requests to the endpoint responsible for displaying a paginated list of teachers.
     * It utilizes the {@code teacherService} to retrieve teacher data based on a provided keyword (optional)
     * and pagination information.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param keyword  an optional search keyword for filtering teachers by name (can be blank)
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return the logical view name {@code teachers/all-teachers} representing the teacher list template
     */
    @GetMapping
    @PreAuthorize("hasAuthority('TEACHERS_READ')")
    public String getPageWithTeachers(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                      @PageableDefault Pageable pageable) {
        Page<TeacherDto> teachersPage = StringUtils.isBlank(keyword)
            ? teacherService.getTeachersPage(pageable)
            : teacherService.getTeacherInPageByEmail(keyword, pageable);

        model.addAttribute(ModelAttributeNames.TEACHERS_ALL_EMAILS_ATTRIBUTE, getTeacherEmails(teacherService.getAll()))
            .addAttribute(ModelAttributeNames.TEACHERS_ATTRIBUTE, teachersPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, teachersPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, teachersPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_TEACHERS_PAGE;
    }

    /**
     * Retrieves the page displaying a particular teacher's details and populates the model with the necessary attributes.
     * <p>
     * This method handles GET requests to the endpoint responsible for displaying the details of a specific teacher.
     * It uses the teacher's ID to fetch the teacher's data and populate the model with the teacher's information for
     * display. If the teacher is not found, it throws a custom exception to handle the error.
     *
     * @param model     the Spring MVC Model object used to store data for the view
     * @param teacherId the ID of the teacher whose details are being requested
     * @return the logical view name representing the particular teacher's details page
     */
    @GetMapping("/{teacherId}")
    @PreAuthorize("hasAuthority('TEACHERS_READ')")
    public String getPageWithParticularTeacher(Model model, @PathVariable("teacherId") long teacherId) {
        model.addAttribute(ModelAttributeNames.TEACHER_ATTRIBUTE, teacherService.getById(teacherId));
        return ViewNames.PARTICULAR_TEACHER;
    }

    /**
     * Displays the form for creating a new teacher.
     * <p>
     * This method is responsible for preparing the model with a new {@link TeacherCreationDto} object
     * to populate the teacher creation form. The user must have the {@code TEACHERS_CREATE} authority
     * to access this form.
     *
     * @param model the {@link Model} object used to pass data to the view.
     * @return the name of the view used for the teacher creation form.
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('TEACHERS_CREATE')")
    public String getCreationForm(Model model) {
        TeacherCreationDto teacher = TeacherCreationDto.builder().build();
        model.addAttribute(ModelAttributeNames.TEACHER_ATTRIBUTE, teacher);

        return ViewNames.TEACHER_CREATION_FORM;
    }

    /**
     * Handles the submission of the teacher creation form and adds a new teacher to the system.
     * <p>
     * This method is responsible for processing the form submission, validating the input data,
     * and saving a new teacher using the provided {@link TeacherCreationDto}. If validation errors
     * occur, the form is redisplayed with the error messages. The user must have the {@code TEACHERS_CREATE}
     * authority to perform this operation.
     *
     * @param teacher       the {@link TeacherCreationDto} containing the teacher's data submitted from the form.
     * @param bindingResult the {@link BindingResult} containing the result of the validation process.
     * @return the name of the view to navigate to after the teacher is successfully added, or the form view if there
     * are validation errors.
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('TEACHERS_CREATE')")
    public String performTeacherAdding(@ModelAttribute("teacher") @Valid TeacherCreationDto teacher,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.TEACHER_CREATION_FORM;
        }

        teacherService.save(teacher);
        return ALL_TEACHERS_REDIRECT_URL;
    }

    /**
     * Retrieves the teacher update form for the specified teacher.
     * <p>
     * This method handles GET requests to the endpoint for displaying the form to update an existing teacher's
     * information. It retrieves the teacher data using the provided {@code teacherId} from the {@link TeacherService}.
     * If the teacher is found, the teacher's data is added to the model for display in the update form.
     *
     * @param model     the Spring MVC Model object used to store data for the view
     * @param teacherId the ID of the teacher whose information is to be updated
     * @return the logical view name for the teacher update form if the teacher is found
     */
    @GetMapping("/{teacherId}/edit")
    @PreAuthorize("hasAuthority('TEACHERS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("teacherId") long teacherId) {
        model.addAttribute(ModelAttributeNames.TEACHER_ATTRIBUTE, teacherService.getById(teacherId));
        return ViewNames.TEACHER_UPDATE_FORM;
    }

    /**
     * Performs the update operation for a teacher's information.
     * <p>
     * This method handles PUT requests to update an existing teacher's data. It validates the provided
     * {@link TeacherDto} object using the {@code BindingResult}. If validation errors occur, it returns the update
     * form for correction. If the validation is successful, the method attempts to update the teacher information
     * using the {@link TeacherService}. If the teacher is found and updated successfully, the user is redirected to
     * the details page of the updated teacher.
     *
     * @param teacher       the {@link TeacherDto} object containing the updated teacher data
     * @param bindingResult the result of the validation for the teacher update
     * @return a redirect URL to the teacher's detail page if the update is successful
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('TEACHERS_UPDATE')")
    public String performTeacherUpdate(@ModelAttribute("teacher") @Valid TeacherDto teacher, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.TEACHER_UPDATE_FORM;
        }

        teacherService.update(teacher);
        return String.format(PARTICULAR_TEACHER_REDIRECT_URL, teacher.getId());
    }

    /**
     * Performs the deletion of a teacher's information.
     * <p>
     * This method handles DELETE requests to remove a teacher from the system. It attempts to delete the teacher
     * based on the provided {@code teacherId}. If the deletion is successful, the user is redirected to the list
     * of all teachers.
     *
     * @param teacherId the ID of the teacher to be deleted
     * @return a redirect URL to the list of all teachers if the deletion is successful
     */
    @DeleteMapping("/{teacherId}/delete")
    @PreAuthorize("hasAuthority('TEACHERS_DELETE')")
    public String performTeacherDeletion(@PathVariable("teacherId") long teacherId) {
        teacherService.deleteById(teacherId);
        return ALL_TEACHERS_REDIRECT_URL;
    }

    /**
     * Displays the form for changing a teacher's password.
     * <p>
     * This method handles GET requests to the endpoint for displaying the password change form for a specific teacher.
     * It creates a {@link PasswordUpdateRequestDto} object populated with the teacher's ID and role, then adds it
     * to the model to be used in the view. The form allows the teacher to update their password.
     *
     * @param model     the Spring MVC Model object used to store data for the view
     * @param teacherId the ID of the teacher whose password is to be changed
     * @return the logical view name for the password update form
     */
    @GetMapping("/{teacherId}/change-pass")
    @PreAuthorize("hasAuthority('TEACHERS_UPDATE')")
    public String getChangePasswordForm(Model model, @PathVariable("teacherId") long teacherId) {
        PasswordUpdateRequestDto passwordUpdateRequest = PasswordUpdateRequestDto.builder()
            .userId(teacherId)
            .roleName(RoleName.TEACHER)
            .build();

        model.addAttribute(ModelAttributeNames.PASSWORD_UPDATE_REQUEST_ATTRIBUTE, passwordUpdateRequest);
        return ViewNames.PASSWORD_UPDATE_FORM;
    }

    /**
     * Handles the password update for a teacher.
     * <p>
     * This method processes the password update request for a specific teacher. It first validates the input
     * using {@link PasswordUpdateRequestDto}. If validation fails, it returns the password update form.
     * If the input is valid, it proceeds to update the teacher's password using the {@link TeacherService}.
     *
     * @param passwordUpdateRequest the password update request containing the new password details
     * @param bindingResult         the result of validating the {@link PasswordUpdateRequestDto}
     * @return a redirection URL to the teacher's page after the update or the password update form if validation fails
     */
    @PatchMapping("/update-pass")
    @PreAuthorize("hasAuthority('TEACHERS_UPDATE')")
    public String performPasswordUpdate(@ModelAttribute("passwordUpdateRequest") @Valid PasswordUpdateRequestDto passwordUpdateRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.PASSWORD_UPDATE_FORM;
        }

        teacherService.updateTeacherPassword(passwordUpdateRequest);
        return String.format(PARTICULAR_TEACHER_REDIRECT_URL, passwordUpdateRequest.getUserId());
    }

    private List<String> getTeacherEmails(Collection<TeacherDto> teachers) {
        return teachers.stream()
            .map(UserDto::getEmail)
            .toList();
    }

}
