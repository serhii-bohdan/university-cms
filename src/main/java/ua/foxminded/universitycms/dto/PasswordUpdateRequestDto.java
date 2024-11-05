package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.util.annotation.ValidPasswordUpdateRequest;

/**
 * Data Transfer Object (DTO) for updating a user's password.
 * <p>
 * This class encapsulates the fields required for a password update operation, including
 * user ID, current password, new password, and confirmation of the new password. It also
 * enforces validation constraints to ensure that all required fields are populated and
 * that password requirements are met.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
@ValidPasswordUpdateRequest
public class PasswordUpdateRequestDto {

    /**
     * The unique identifier of the user whose password is being updated.
     */
    @NotNull
    private Long userId;

    /**
     * The user's current password.
     */
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    /**
     * The new password the user wants to set.
     */
    @NotBlank(message = "New password is required")
    private String newPassword;

    /**
     * Confirmation of the new password.
     */
    @NotBlank(message = "Please confirm the new password")
    private String confirmNewPassword;

}
