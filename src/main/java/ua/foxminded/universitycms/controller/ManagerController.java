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
import ua.foxminded.universitycms.dto.ManagerCreationDto;
import ua.foxminded.universitycms.dto.ManagerDto;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.service.ManagerService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Spring MVC Controller for handling web requests related to managing and displaying managers.
 * Provides endpoints for retrieving paginated lists of managers, creating, updating, deleting
 * managers, and updating passwords. Uses {@link ManagerService} for business logic. Annotated
 * with {@code @Controller} and {@code @RequiredArgsConstructor}.
 *
 * @author Serhii Bohdan
 * @see ManagerService
 * @see ModelAttributeNames
 * @see ViewNames
 */
@Controller
@RequiredArgsConstructor
@RequestMapping({"/ui/v1/managers"})
public class ManagerController {

    /**
     * Redirect URL to the page displaying all managers after operations.
     */
    private static final String ALL_MANAGERS_REDIRECT_URL = "redirect:/ui/v1/managers";

    /**
     * Redirect URL template to a specific manager's page, with a placeholder for manager ID.
     */
    private static final String PARTICULAR_MANAGER_REDIRECT_URL = "redirect:/ui/v1/managers/%s";

    /**
     * Service for interacting with manager data and performing business logic operations.
     */
    private final ManagerService managerService;

    /**
     * Displays a paginated list of managers, optionally filtered by keyword.
     * Handles GET requests to {@code /ui/v1/managers}. Retrieves managers via
     * {@link ManagerService#findManagers} and adds pagination data and emails to the model.
     * Requires {@code MANAGERS_READ} authority.
     *
     * @param model    the {@link Model} to store view data
     * @param keyword  optional keyword to filter managers by email; may be blank
     * @param pageable pagination info from {@link PageableDefault}
     * @return view name {@link ViewNames#ALL_MANAGERS_PAGE} for the manager list
     */
    @GetMapping
    @PreAuthorize("hasAuthority('MANAGERS_READ')")
    public String getPageWithManagers(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                      @PageableDefault Pageable pageable) {
        Page<ManagerDto> managersPage = managerService.findManagers(pageable, keyword);

        model.addAttribute(ModelAttributeNames.MANAGERS_ALL_EMAILS_ATTRIBUTE, managerService.extractManagerEmails(managerService.getAll()))
            .addAttribute(ModelAttributeNames.MANAGERS_ATTRIBUTE, managersPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, managersPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, managersPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_MANAGERS_PAGE;
    }

    /**
     * Displays details of a specific manager.
     * Handles GET requests to {@code /ui/v1/managers/{managerId}}. Retrieves manager data via
     * {@link ManagerService#getById} and adds it to the model. Requires {@code MANAGERS_READ}.
     *
     * @param model     the {@link Model} to store view data
     * @param managerId the ID of the manager to display
     * @return view name {@link ViewNames#PARTICULAR_MANAGER} for manager details
     */
    @GetMapping("/{managerId}")
    @PreAuthorize("hasAuthority('MANAGERS_READ')")
    public String getPageWithParticularManager(Model model, @PathVariable("managerId") long managerId) {
        model.addAttribute(ModelAttributeNames.MANAGER_ATTRIBUTE, managerService.getById(managerId));
        return ViewNames.PARTICULAR_MANAGER;
    }

    /**
     * Displays the form for creating a new manager.
     * Handles GET requests to {@code /ui/v1/managers/new}. Prepares a {@link ManagerCreationDto}
     * for the form. Requires {@code MANAGERS_CREATE} authority.
     *
     * @param model the {@link Model} to store form data
     * @return view name {@link ViewNames#MANAGER_CREATION_FORM} for the creation form
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('MANAGERS_CREATE')")
    public String getCreationForm(Model model) {
        ManagerCreationDto manager = ManagerCreationDto.builder().build();
        model.addAttribute(ModelAttributeNames.MANAGER_ATTRIBUTE, manager)
            .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
        return ViewNames.MANAGER_CREATION_FORM;
    }

    /**
     * Processes the submission of the manager creation form via a POST request to {@code /ui/v1/managers/add}.
     * Validates the {@link ManagerCreationDto} and saves it using {@link ManagerService#save}. Requires
     * {@code MANAGERS_CREATE} authority. On validation errors, returns the form view with time zones;
     * otherwise, redirects to the manager list.
     *
     * @param model         the {@link Model} for adding attributes like time zones on errors
     * @param manager       the {@link ManagerCreationDto} with form data, must be valid
     * @param bindingResult the {@link BindingResult} containing validation results for the DTO
     * @return redirect to {@link #ALL_MANAGERS_REDIRECT_URL} on success, or {@link ViewNames#MANAGER_CREATION_FORM}
     * on errors
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('MANAGERS_CREATE')")
    public String performManagerAdding(Model model, @ModelAttribute("manager") @Valid ManagerCreationDto manager,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
            return ViewNames.MANAGER_CREATION_FORM;
        }

        managerService.save(manager);
        return ALL_MANAGERS_REDIRECT_URL;
    }

    /**
     * Displays the form for updating a manager's information.
     * Handles GET requests to {@code /ui/v1/managers/{managerId}/edit}. Retrieves manager data
     * via {@link ManagerService#getById} for the form. Requires {@code MANAGERS_UPDATE}.
     *
     * @param model     the {@link Model} to store form data
     * @param managerId the ID of the manager to update
     * @return view name {@link ViewNames#MANAGER_UPDATE_FORM} for the update form
     */
    @GetMapping("/{managerId}/edit")
    @PreAuthorize("hasAuthority('MANAGERS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("managerId") long managerId) {
        ManagerDto manager = managerService.getById(managerId);
        model.addAttribute(ModelAttributeNames.MANAGER_ATTRIBUTE, manager)
            .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
        return ViewNames.MANAGER_UPDATE_FORM;
    }

    /**
     * Processes the update of a manager's information via a PUT request to {@code /ui/v1/managers/update}.
     * Validates the {@link ManagerDto} and updates it using {@link ManagerService#update}. Requires
     * {@code MANAGERS_UPDATE} authority. On validation errors, returns the form view with time zones;
     * otherwise, redirects to the manager's page.
     *
     * @param model         the {@link Model} for adding attributes like time zones on errors
     * @param manager       the {@link ManagerDto} with updated data, must be valid
     * @param bindingResult the {@link BindingResult} containing validation results for the DTO
     * @return redirect to {@link #PARTICULAR_MANAGER_REDIRECT_URL} with manager ID, or {@link ViewNames#MANAGER_UPDATE_FORM}
     * on errors
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('MANAGERS_UPDATE')")
    public String performManagerUpdate(Model model, @ModelAttribute("manager") @Valid ManagerDto manager,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST);
            return ViewNames.MANAGER_UPDATE_FORM;
        }

        managerService.update(manager);
        return PARTICULAR_MANAGER_REDIRECT_URL.formatted(manager.getId());
    }

    /**
     * Deletes a manager from the system.
     * Handles DELETE requests to {@code /ui/v1/managers/{managerId}/delete}. Deletes manager via
     * {@link ManagerService#deleteById} and redirects. Requires {@code MANAGERS_DELETE}.
     *
     * @param managerId the ID of the manager to delete
     * @return redirect to {@link #ALL_MANAGERS_REDIRECT_URL}
     */
    @DeleteMapping("/{managerId}/delete")
    @PreAuthorize("hasAuthority('MANAGERS_DELETE')")
    public String performManagerDeletion(@PathVariable("managerId") long managerId) {
        managerService.deleteById(managerId);
        return ALL_MANAGERS_REDIRECT_URL;
    }

    /**
     * Displays the password change form for a manager.
     * Handles GET requests to {@code /ui/v1/managers/{managerId}/change-pass}. Prepares a
     * {@link PasswordUpdateRequestDto} for the form. Requires {@code MANAGERS_UPDATE}.
     *
     * @param model     the {@link Model} to store form data
     * @param managerId the ID of the manager whose password will change
     * @return view name {@link ViewNames#PASSWORD_UPDATE_FORM} for the form
     */
    @GetMapping("/{managerId}/change-pass")
    @PreAuthorize("hasAuthority('MANAGERS_UPDATE')")
    public String getChangePasswordForm(Model model, @PathVariable("managerId") long managerId) {
        PasswordUpdateRequestDto passwordUpdateRequestDto = PasswordUpdateRequestDto.builder()
            .userId(managerId)
            .roleName(RoleName.MANAGER)
            .build();

        model.addAttribute(ModelAttributeNames.PASSWORD_UPDATE_REQUEST_ATTRIBUTE, passwordUpdateRequestDto);
        return ViewNames.PASSWORD_UPDATE_FORM;
    }

    /**
     * Updates a manager's password.
     * Handles PATCH requests to {@code /ui/v1/managers/update-pass}. Validates
     * {@link PasswordUpdateRequestDto} and updates via {@link ManagerService#updateManagerPassword}.
     * Requires {@code MANAGERS_UPDATE}.
     *
     * @param passwordUpdateRequest the DTO with new password details
     * @param bindingResult         validation results for the DTO
     * @return redirect to manager page or form view on errors
     */
    @PatchMapping("/update-pass")
    @PreAuthorize("hasAuthority('MANAGERS_UPDATE')")
    public String performPasswordUpdate(@ModelAttribute("passwordUpdateRequest") @Valid PasswordUpdateRequestDto passwordUpdateRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.PASSWORD_UPDATE_FORM;
        }

        managerService.updateManagerPassword(passwordUpdateRequest);
        return PARTICULAR_MANAGER_REDIRECT_URL.formatted(passwordUpdateRequest.getUserId());
    }

}
