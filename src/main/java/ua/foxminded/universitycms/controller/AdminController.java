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
import ua.foxminded.universitycms.dto.AdminCreationDto;
import ua.foxminded.universitycms.dto.AdminDto;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.service.AdminService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Spring MVC Controller for handling web requests related to managing and displaying admin users.
 * Provides endpoints for retrieving paginated lists of admins, creating new admins, and handling
 * related operations. Uses {@link AdminService} for business logic. Annotated with
 * {@code @Controller} and {@code @RequiredArgsConstructor}.
 *
 * @author Serhii Bohdan
 * @see AdminService
 * @see ModelAttributeNames
 * @see ViewNames
 */
@Controller
@RequiredArgsConstructor
@RequestMapping({"/ui/v1/admins"})
public class AdminController {

    /**
     * Redirect URL to the page displaying all admins after operations.
     */
    private static final String ALL_ADMINS_REDIRECT_URL = "redirect:/ui/v1/admins";

    /**
     * Redirect URL template for navigating to the details page of a specific admin.
     */
    private static final String PARTICULAR_ADMIN_REDIRECT_URL = "redirect:/ui/v1/admins/%s";

    /**
     * Service for interacting with admin data and performing business logic operations.
     */
    private final AdminService adminService;

    /**
     * Displays a paginated list of admins, optionally filtered by keyword.
     * Handles GET requests to {@code /ui/v1/admins}. Retrieves admins via
     * {@link AdminService#findAdmins} and adds pagination data and emails to the model.
     * Requires {@code ADMINS_READ} authority.
     *
     * @param model    the {@link Model} to store view data
     * @param keyword  optional keyword to filter admins by email; may be blank
     * @param pageable pagination info from {@link PageableDefault}
     * @return view name {@link ViewNames#ALL_ADMINS_PAGE} for the admin list
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMINS_READ')")
    public String getPageWithAdmins(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                    @PageableDefault Pageable pageable) {
        Page<AdminDto> adminsPage = adminService.findAdmins(pageable, keyword);

        model.addAttribute(ModelAttributeNames.ADMINS_ALL_EMAILS_ATTRIBUTE, adminService.extractAdminEmails(adminService.getAll()))
            .addAttribute(ModelAttributeNames.ADMINS_ATTRIBUTE, adminsPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, adminsPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, adminsPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_ADMINS_PAGE;
    }

    /**
     * Displays the details page for a specific admin.
     * <p>
     * Handles GET requests to {@code /ui/v1/admins/{adminId}}. Retrieves the admin's data using
     * {@link AdminService#getById} and adds it to the model. Requires {@code ADMINS_READ} authority.
     *
     * @param model   the {@link Model} to store the admin data
     * @param adminId the ID of the admin to display
     * @return the view name {@link ViewNames#PARTICULAR_ADMIN} for the admin details page
     */
    @GetMapping("/{adminId}")
    @PreAuthorize("hasAuthority('ADMINS_READ')")
    public String getPageWithParticularAdmin(Model model, @PathVariable("adminId") long adminId) {
        model.addAttribute(ModelAttributeNames.ADMIN_ATTRIBUTE, adminService.getById(adminId));
        return ViewNames.PARTICULAR_ADMIN;
    }

    /**
     * Displays the form for creating a new admin.
     * Handles GET requests to {@code /ui/v1/admins/new}. Prepares a {@link AdminCreationDto} for
     * the form. Requires {@code ADMINS_CREATE} authority.
     *
     * @param model the {@link Model} to store form data
     * @return view name {@link ViewNames#ADMIN_CREATION_FORM} for the creation form
     */
    @GetMapping("/new")
    @PreAuthorize("hasAnyAuthority('ADMINS_CREATE')")
    public String getCreationForm(Model model) {
        AdminCreationDto admin = AdminCreationDto.builder().build();
        model.addAttribute(ModelAttributeNames.ADMIN_ATTRIBUTE, admin)
            .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
        return ViewNames.ADMIN_CREATION_FORM;
    }

    /**
     * Processes the submission of the admin creation form via a POST request to {@code /ui/v1/admins/add}.
     * Validates the {@link AdminCreationDto} and saves it using {@link AdminService#save}. Requires
     * {@code ADMINS_CREATE} authority. On validation errors, returns the form view with time zones;
     * otherwise, redirects to the admin list.
     *
     * @param model         the {@link Model} for adding attributes like time zones on errors
     * @param admin         the {@link AdminCreationDto} with form data, must be valid
     * @param bindingResult the {@link BindingResult} containing validation results for the DTO
     * @return redirect to {@link #ALL_ADMINS_REDIRECT_URL} on success, or {@link ViewNames#ADMIN_CREATION_FORM} on errors
     */
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMINS_CREATE')")
    public String performAdminAdding(Model model, @ModelAttribute("admin") @Valid AdminCreationDto admin,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
            return ViewNames.ADMIN_CREATION_FORM;
        }

        adminService.save(admin);
        return ALL_ADMINS_REDIRECT_URL;
    }

    /**
     * Displays the form for updating an admin's details.
     * <p>
     * Handles GET requests to {@code /ui/v1/admins/{adminId}/edit}. Retrieves the admin's data using
     * {@link AdminService#getById} and populates the model with the admin and available time zones.
     * Requires {@code ADMINS_UPDATE} authority.
     *
     * @param model   the {@link Model} to store form attributes
     * @param adminId the ID of the admin to update
     * @return the view name {@link ViewNames#ADMIN_UPDATE_FORM} for the admin update form
     */
    @GetMapping("/{adminId}/edit")
    @PreAuthorize("hasAuthority('ADMINS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("adminId") long adminId) {
        AdminDto admin = adminService.getById(adminId);
        model.addAttribute(ModelAttributeNames.ADMIN_ATTRIBUTE, admin)
            .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
        return ViewNames.ADMIN_UPDATE_FORM;
    }

    /**
     * Processes the update of an admin's details via a PUT request.
     * <p>
     * Validates the submitted {@link AdminDto} and updates the admin using {@link AdminService#update}.
     * If validation fails, returns the update form with time zones; otherwise, redirects to the admin's
     * details page. Requires {@code ADMINS_UPDATE} authority.
     *
     * @param model         the {@link Model} to store attributes if validation fails
     * @param admin         the {@link AdminDto} containing updated data, must be valid
     * @param bindingResult the {@link BindingResult} containing validation results
     * @return redirect to {@link #PARTICULAR_ADMIN_REDIRECT_URL} on success, or
     * {@link ViewNames#ADMIN_UPDATE_FORM} on validation errors
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMINS_UPDATE')")
    public String performAdminUpdate(Model model, @ModelAttribute("admin") @Valid AdminDto admin,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
            return ViewNames.ADMIN_UPDATE_FORM;
        }

        adminService.update(admin);
        return PARTICULAR_ADMIN_REDIRECT_URL.formatted(admin.getId());
    }

    /**
     * Deletes an admin via a DELETE request.
     * <p>
     * Removes the admin identified by {@code adminId} using {@link AdminService#deleteById} and redirects
     * to the list of all admins. Requires {@code ADMINS_DELETE} authority.
     *
     * @param adminId the ID of the admin to delete
     * @return redirect to {@link #ALL_ADMINS_REDIRECT_URL}
     */
    @DeleteMapping("/{adminId}/delete")
    @PreAuthorize("hasAuthority('ADMINS_DELETE')")
    public String performAdminDeletion(@PathVariable("adminId") long adminId) {
        adminService.deleteById(adminId);
        return ALL_ADMINS_REDIRECT_URL;
    }

    /**
     * Displays the form for changing an admin's password.
     * <p>
     * Handles GET requests to {@code /ui/v1/admins/{adminId}/change-pass}. Initializes a
     * {@link PasswordUpdateRequestDto} with the admin's ID and role, and adds it to the model.
     * Requires {@code ADMINS_UPDATE} authority.
     *
     * @param model   the {@link Model} to store the password update form data
     * @param adminId the ID of the admin whose password is to be changed
     * @return the view name {@link ViewNames#PASSWORD_UPDATE_FORM} for the password update form
     */
    @GetMapping("/{adminId}/change-pass")
    @PreAuthorize("hasAuthority('ADMINS_UPDATE')")
    public String getChangePasswordForm(Model model, @PathVariable("adminId") long adminId) {
        PasswordUpdateRequestDto passwordUpdateRequestDto = PasswordUpdateRequestDto.builder()
            .userId(adminId)
            .roleName(RoleName.ADMIN)
            .build();

        model.addAttribute(ModelAttributeNames.PASSWORD_UPDATE_REQUEST_ATTRIBUTE, passwordUpdateRequestDto);
        return ViewNames.PASSWORD_UPDATE_FORM;
    }

    /**
     * Processes the update of an admin's password via a PATCH request.
     * <p>
     * Validates the submitted {@link PasswordUpdateRequestDto} and updates the admin's password using
     * {@link AdminService#updateAdminPassword}. If validation fails, returns the password update form;
     * otherwise, redirects to the admin's details page. Requires {@code ADMINS_UPDATE} authority.
     *
     * @param passwordUpdateRequest the {@link PasswordUpdateRequestDto} containing password update data, must be valid
     * @param bindingResult         the {@link BindingResult} containing validation results
     * @return redirect to {@link #PARTICULAR_ADMIN_REDIRECT_URL} on success, or
     * {@link ViewNames#PASSWORD_UPDATE_FORM} on validation errors
     */
    @PatchMapping("/update-pass")
    @PreAuthorize("hasAuthority('ADMINS_UPDATE')")
    public String performPasswordUpdate(@ModelAttribute("passwordUpdateRequest") @Valid PasswordUpdateRequestDto passwordUpdateRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.PASSWORD_UPDATE_FORM;
        }

        adminService.updateAdminPassword(passwordUpdateRequest);
        return PARTICULAR_ADMIN_REDIRECT_URL.formatted(passwordUpdateRequest.getUserId());
    }

}
