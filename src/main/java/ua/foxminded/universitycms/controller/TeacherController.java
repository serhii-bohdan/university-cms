package ua.foxminded.universitycms.controller;

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
import ua.foxminded.universitycms.dto.TeacherCreationDto;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.service.TeacherService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Spring MVC Controller for handling teacher-related requests under the {@code /ui/v1/teachers} path.
 * Manages operations such as displaying teacher lists, creating, updating, and deleting teachers,
 * and updating passwords. Uses {@link TeacherService} for business logic. Annotated with
 * {@code @Controller} for MVC handling and {@code @RequiredArgsConstructor} for dependency injection.
 *
 * @author Serhii Bohdan
 * @see TeacherService
 * @see ModelAttributeNames
 * @see ViewNames
 */
@Controller
@RequiredArgsConstructor
@RequestMapping({"/ui/v1/teachers"})
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
     * Service for interacting with teacher data and performing business logic operations.
     */
    private final TeacherService teacherService;

    /**
     * Displays a paginated list of teachers, optionally filtered by keyword.
     * Handles GET requests to {@code /ui/v1/teachers}. Retrieves teachers via
     * {@link TeacherService#findTeachers} and adds pagination data and emails to the model.
     * Requires {@code TEACHERS_READ} authority.
     *
     * @param model    the {@link Model} to store view data
     * @param keyword  optional keyword to filter teachers by email; may be blank
     * @param pageable pagination info from {@link PageableDefault}
     * @return view name {@link ViewNames#ALL_TEACHERS_PAGE} for the teacher list
     */
    @GetMapping
    @PreAuthorize("hasAuthority('TEACHERS_READ')")
    public String getPageWithTeachers(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                      @PageableDefault Pageable pageable) {
        Page<TeacherDto> teachersPage = teacherService.findTeachers(pageable, keyword);

        model.addAttribute(ModelAttributeNames.TEACHERS_ALL_EMAILS_ATTRIBUTE, teacherService.extractTeacherEmails(teacherService.getAll()))
            .addAttribute(ModelAttributeNames.TEACHERS_ATTRIBUTE, teachersPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, teachersPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, teachersPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_TEACHERS_PAGE;
    }

    /**
     * Displays details of a specific teacher.
     * Handles GET requests to {@code /ui/v1/teachers/{teacherId}}. Retrieves teacher data via
     * {@link TeacherService#getById} and adds it to the model. Requires {@code TEACHERS_READ}.
     *
     * @param model     the {@link Model} to store view data
     * @param teacherId the ID of the teacher to display
     * @return view name {@link ViewNames#PARTICULAR_TEACHER} for teacher details
     */
    @GetMapping("/{teacherId}")
    @PreAuthorize("hasAuthority('TEACHERS_READ')")
    public String getPageWithParticularTeacher(Model model, @PathVariable("teacherId") long teacherId) {
        model.addAttribute(ModelAttributeNames.TEACHER_ATTRIBUTE, teacherService.getById(teacherId));
        return ViewNames.PARTICULAR_TEACHER;
    }

    /**
     * Displays the form for creating a new teacher.
     * Handles GET requests to {@code /ui/v1/teachers/new}. Prepares a {@link TeacherCreationDto}
     * for the form. Requires {@code TEACHERS_CREATE} authority.
     *
     * @param model the {@link Model} to store form data
     * @return view name {@link ViewNames#TEACHER_CREATION_FORM} for the creation form
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('TEACHERS_CREATE')")
    public String getCreationForm(Model model) {
        TeacherCreationDto teacher = TeacherCreationDto.builder().build();
        model.addAttribute(ModelAttributeNames.TEACHER_ATTRIBUTE, teacher)
            .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
        return ViewNames.TEACHER_CREATION_FORM;
    }

    /**
     * Processes the submission of the teacher creation form via a POST request to {@code /ui/v1/teachers/add}.
     * Validates the {@link TeacherCreationDto} and saves it using {@link TeacherService#save}. Requires
     * {@code TEACHERS_CREATE} authority. On validation errors, returns the form view with time zones;
     * otherwise, redirects to the teacher list.
     *
     * @param model         the {@link Model} for adding attributes like time zones on errors
     * @param teacher       the {@link TeacherCreationDto} with form data, must be valid
     * @param bindingResult the {@link BindingResult} containing validation results for the DTO
     * @return redirect to {@link #ALL_TEACHERS_REDIRECT_URL} on success, or {@link ViewNames#TEACHER_CREATION_FORM}
     * on errors
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('TEACHERS_CREATE')")
    public String performTeacherAdding(Model model, @ModelAttribute("teacher") @Valid TeacherCreationDto teacher,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
            return ViewNames.TEACHER_CREATION_FORM;
        }

        teacherService.save(teacher);
        return ALL_TEACHERS_REDIRECT_URL;
    }

    /**
     * Displays the form for updating a teacher's information.
     * Handles GET requests to {@code /ui/v1/teachers/{teacherId}/edit}. Retrieves teacher data via
     * {@link TeacherService#getById} for the form. Requires {@code TEACHERS_UPDATE}.
     *
     * @param model     the {@link Model} to store form data
     * @param teacherId the ID of the teacher to update
     * @return view name {@link ViewNames#TEACHER_UPDATE_FORM} for the update form
     */
    @GetMapping("/{teacherId}/edit")
    @PreAuthorize("hasAuthority('TEACHERS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("teacherId") long teacherId) {
        model.addAttribute(ModelAttributeNames.TEACHER_ATTRIBUTE, teacherService.getById(teacherId))
            .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
        return ViewNames.TEACHER_UPDATE_FORM;
    }

    /**
     * Processes the update of a teacher's information via a PUT request to {@code /ui/v1/teachers/update}.
     * Validates the {@link TeacherDto} and updates it using {@link TeacherService#update}. Requires
     * {@code TEACHERS_UPDATE} authority. On validation errors, returns the form view with time zones;
     * otherwise, redirects to the teacher's page.
     *
     * @param model         the {@link Model} for adding attributes like time zones on errors
     * @param teacher       the {@link TeacherDto} with updated data, must be valid
     * @param bindingResult the {@link BindingResult} containing validation results for the DTO
     * @return redirect to {@link #PARTICULAR_TEACHER_REDIRECT_URL} with teacher ID, or {@link ViewNames#TEACHER_UPDATE_FORM}
     * on errors
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('TEACHERS_UPDATE')")
    public String performTeacherUpdate(Model model, @ModelAttribute("teacher") @Valid TeacherDto teacher, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
            return ViewNames.TEACHER_UPDATE_FORM;
        }

        teacherService.update(teacher);
        return PARTICULAR_TEACHER_REDIRECT_URL.formatted(teacher.getId());
    }

    /**
     * Deletes a teacher from the system.
     * Handles DELETE requests to {@code /ui/v1/teachers/{teacherId}/delete}. Deletes teacher via
     * {@link TeacherService#deleteById} and redirects. Requires {@code TEACHERS_DELETE}.
     *
     * @param teacherId the ID of the teacher to delete
     * @return redirect to {@link #ALL_TEACHERS_REDIRECT_URL}
     */
    @DeleteMapping("/{teacherId}/delete")
    @PreAuthorize("hasAuthority('TEACHERS_DELETE')")
    public String performTeacherDeletion(@PathVariable("teacherId") long teacherId) {
        teacherService.deleteById(teacherId);
        return ALL_TEACHERS_REDIRECT_URL;
    }

    /**
     * Displays the password change form for a teacher.
     * Handles GET requests to {@code /ui/v1/teachers/{teacherId}/change-pass}. Prepares a
     * {@link PasswordUpdateRequestDto} for the form. Requires {@code TEACHERS_UPDATE}.
     *
     * @param model     the {@link Model} to store form data
     * @param teacherId the ID of the teacher whose password will change
     * @return view name {@link ViewNames#PASSWORD_UPDATE_FORM} for the form
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
     * Updates a teacher's password.
     * Handles PATCH requests to {@code /ui/v1/teachers/update-pass}. Validates
     * {@link PasswordUpdateRequestDto} and updates via {@link TeacherService#updateTeacherPassword}.
     * Requires {@code TEACHERS_UPDATE}.
     *
     * @param passwordUpdateRequest the DTO with new password details
     * @param bindingResult         validation results for the DTO
     * @return redirect to teacher page or form view on errors
     */
    @PatchMapping("/update-pass")
    @PreAuthorize("hasAuthority('TEACHERS_UPDATE')")
    public String performPasswordUpdate(@ModelAttribute("passwordUpdateRequest") @Valid PasswordUpdateRequestDto passwordUpdateRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.PASSWORD_UPDATE_FORM;
        }

        teacherService.updateTeacherPassword(passwordUpdateRequest);
        return PARTICULAR_TEACHER_REDIRECT_URL.formatted(passwordUpdateRequest.getUserId());
    }

}
