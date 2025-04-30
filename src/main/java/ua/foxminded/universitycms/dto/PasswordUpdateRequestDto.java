package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.util.annotation.ValidPasswordUpdateRequest;

/**
 * Data Transfer Object (DTO) for handling password update requests in the university management system.
 * <p>
 * This class encapsulates the data required to process a user's password update, including the user's
 * identifier, role, current password, new password, and its confirmation. It facilitates secure data
 * transfer between application layers, with validation constraints ensuring all fields are provided
 * and meet specific requirements. The {@link ValidPasswordUpdateRequest} annotation enforces additional
 * password update logic, while {@link NotNull} and {@link NotBlank} annotations validate the presence
 * of critical fields.
 *
 * @author Serhii Bohdan
 * @see RoleName
 * @see ValidPasswordUpdateRequest
 * @see jakarta.validation.constraints.NotNull
 * @see jakarta.validation.constraints.NotBlank
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
@ValidPasswordUpdateRequest
public class PasswordUpdateRequestDto {

    /**
     * The unique identifier of the user requesting the password update.
     * <p>
     * This field is mandatory, as enforced by the {@link NotNull} constraint, and identifies the user
     * whose password is being updated.
     */
    @NotNull
    private Long userId;

    /**
     * The role name of the user requesting the password update.
     * <p>
     * This field is mandatory, as enforced by the {@link NotNull} constraint, and specifies the user's
     * role within the system using the {@link RoleName} enumeration.
     */
    @NotNull
    private RoleName roleName;

    /**
     * The user's current password.
     * <p>
     * This field is mandatory, as enforced by the {@link NotBlank} constraint, and represents the
     * existing password that must be verified before updating.
     */
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    /**
     * The new password to be set for the user.
     * <p>
     * This field is mandatory, as enforced by the {@link NotBlank} constraint, and specifies the new
     * password the user intends to use.
     */
    @NotBlank(message = "New password is required")
    private String newPassword;

    /**
     * Confirmation of the new password.
     * <p>
     * This field is mandatory, as enforced by the {@link NotBlank} constraint, and must match the
     * {@code newPassword} field to confirm the user's intent.
     */
    @NotBlank(message = "Please confirm the new password")
    private String confirmNewPassword;

}
