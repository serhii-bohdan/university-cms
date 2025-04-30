package ua.foxminded.universitycms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.AuthorizedUserService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Spring MVC Controller for handling authorized user-related requests under the {@code /ui/v1/iam} path.
 * Manages operations such as displaying user profiles, updating passwords, and editing full names.
 * Uses {@link AuthorizedUserService} for business logic. Annotated with {@code @Controller} and
 * {@code @RequiredArgsConstructor}.
 *
 * @author Serhii Bohdan
 * @see AuthorizedUserService
 * @see ModelAttributeNames
 * @see ViewNames
 */
@Controller
@RequiredArgsConstructor
@RequestMapping({"/ui/v1/iam"})
public class AuthorizedUserController {

    /**
     * Redirect URL to the authorized user's profile page after operations.
     */
    private static final String AUTHORIZED_USER_PROFILE_REDIRECT_URL = "redirect:/ui/v1/iam";

    /**
     * Service for interacting with authorized user data and performing business logic operations.
     */
    private final AuthorizedUserService authorizedUserService;

    /**
     * Displays the profile page for the authenticated user.
     * Handles GET requests to {@code /ui/v1/iam}. Retrieves user data via
     * {@link AuthorizedUserService#findUserByUserDetails} and adds it to the model.
     *
     * @param model             the {@link Model} to store view data
     * @param customUserDetails authenticated user details from {@link CustomUserDetails}
     * @return view name {@link ViewNames#AUTHORIZED_USER_PROFILE_PAGE} for the user profile
     */
    @GetMapping
    public String getUserProfilePage(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute(ModelAttributeNames.USER_ATTRIBUTE, authorizedUserService.findUserByUserDetails(customUserDetails));
        return ViewNames.AUTHORIZED_USER_PROFILE_PAGE;
    }

    /**
     * Displays the password change form for the authenticated user.
     * Handles GET requests to {@code /ui/v1/iam/change-pass}. Prepares a
     * {@link PasswordUpdateRequestDto} with user details for the form.
     *
     * @param model             the {@link Model} to store form data
     * @param customUserDetails authenticated user details from {@link CustomUserDetails}
     * @return view name {@link ViewNames#AUTHORIZED_USER_PASSWORD_UPDATE_FORM} for the form
     */
    @GetMapping("/change-pass")
    public String getChangePasswordForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        PasswordUpdateRequestDto passwordUpdateRequest = PasswordUpdateRequestDto.builder()
            .userId(customUserDetails.getId())
            .roleName(customUserDetails.getRoleName())
            .build();

        model.addAttribute(ModelAttributeNames.PASSWORD_UPDATE_REQUEST_ATTRIBUTE, passwordUpdateRequest);
        return ViewNames.AUTHORIZED_USER_PASSWORD_UPDATE_FORM;
    }

    /**
     * Processes the password update for the authenticated user.
     * Handles PATCH requests to {@code /ui/v1/iam/update-pass}. Validates
     * {@link PasswordUpdateRequestDto} and updates via {@link AuthorizedUserService#updateUserPassword}.
     * Returns the form on errors.
     *
     * @param passwordUpdateRequest the DTO with new password details
     * @param bindingResult         validation results for the DTO
     * @return redirect to {@link #AUTHORIZED_USER_PROFILE_REDIRECT_URL} or form view on errors
     */
    @PatchMapping("/update-pass")
    public String performPasswordUpdate(@ModelAttribute("passwordUpdateRequest") @Valid PasswordUpdateRequestDto passwordUpdateRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.AUTHORIZED_USER_PASSWORD_UPDATE_FORM;
        }

        authorizedUserService.updateUserPassword(passwordUpdateRequest);
        return AUTHORIZED_USER_PROFILE_REDIRECT_URL;
    }

    /**
     * Displays the form for updating the authenticated user's full name.
     * Handles GET requests to {@code /ui/v1/iam/edit-name}. Prepares a {@link FullName} object with
     * current user details for the form.
     *
     * @param model             the {@link Model} to store form data
     * @param customUserDetails authenticated user details from {@link CustomUserDetails}
     * @return view name {@link ViewNames#AUTHORIZED_USER_NAME_UPDATE_FORM} for the form
     */
    @GetMapping("/edit-name")
    public String getNameUpdateForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute(ModelAttributeNames.USER_FULL_NAME_ATTRIBUTE, new FullName(customUserDetails.getFirstName(),
            customUserDetails.getLastName()));
        return ViewNames.AUTHORIZED_USER_NAME_UPDATE_FORM;
    }

    /**
     * Processes the update of the authenticated user's full name.
     * Handles PATCH requests to {@code /ui/v1/iam/name-update}. Validates {@link FullName} and
     * updates via {@link AuthorizedUserService#updateFullNameByUserDetails}. Returns the form on errors.
     *
     * @param customUserDetails authenticated user details from {@link CustomUserDetails}
     * @param fullName          the {@link FullName} with updated name data
     * @param bindingResult     validation results for the DTO
     * @return redirect to {@link #AUTHORIZED_USER_PROFILE_REDIRECT_URL} or form view on errors
     */
    @PatchMapping("/name-update")
    public String performUserNameUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        @ModelAttribute("userFullName") @Valid FullName fullName,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.AUTHORIZED_USER_NAME_UPDATE_FORM;
        }

        authorizedUserService.updateFullNameByUserDetails(customUserDetails, fullName);
        return AUTHORIZED_USER_PROFILE_REDIRECT_URL;
    }

}
