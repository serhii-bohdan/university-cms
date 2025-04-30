package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.util.annotation.UniqueUserEmail;
import java.time.ZonedDateTime;

/**
 * Abstract Data Transfer Object (DTO) representing a user entity in the university management system.
 * <p>
 * This class extends {@link AbstractDto} to inherit a unique identifier and defines attributes specific
 * to a user, such as personal details, email, role information, and timestamps. It serves as a base
 * for user-related DTOs, facilitating data transfer between application layers while enforcing
 * validation constraints on key fields. The {@link UniqueUserEmail} annotation ensures email uniqueness
 * across users, and other Jakarta validation annotations enforce data integrity.
 *
 * @author Serhii Bohdan
 * @see AbstractDto
 * @see UniqueUserEmail
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.Email
 * @see jakarta.validation.constraints.Size
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@UniqueUserEmail
public abstract class UserDto extends AbstractDto {

    /**
     * The user's first name.
     * <p>
     * This field is mandatory and must not exceed 255 characters, as enforced by validation constraints.
     */
    @NotBlank(message = "First name is mandatory")
    @Size(max = 255, message = "First name must be 255 characters or less")
    private String firstName;

    /**
     * The user's last name.
     * <p>
     * This field is mandatory and must not exceed 255 characters, as enforced by validation constraints.
     */
    @NotBlank(message = "Last name is mandatory")
    @Size(max = 255, message = "Last name must be 255 characters or less")
    private String lastName;

    /**
     * The user's email address.
     * <p>
     * This field is mandatory and must conform to a valid email format. It is also subject to the
     * {@link UniqueUserEmail} constraint, ensuring uniqueness across user records.
     */
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email address is not valid")
    private String email;

    /**
     * The user's time zone offset from UTC, e.g., "+02:00" or "-05:00".
     * <p>
     * This field is mandatory as enforced by the {@link NotBlank} constraint.
     */
    @NotBlank(message = "Time zone offset cannot be empty")
    private String locationZoneOffset;

    /**
     * The ID of the role assigned to the user.
     * <p>
     * This field represents the unique identifier of the user's role within the system.
     */
    private Long roleId;

    /**
     * The name of the role assigned to the user.
     * <p>
     * This field provides a human-readable name for the user's role, complementing the {@code roleId}.
     */
    private String roleName;

    /**
     * The date and time when the user record was created, including time zone information.
     * <p>
     * This field tracks the creation timestamp of the user entity in the system.
     */
    private ZonedDateTime createdAt;

    /**
     * The date and time when the user record was last updated, including time zone information.
     * <p>
     * This field tracks the most recent update timestamp of the user entity in the system.
     */
    private ZonedDateTime updatedAt;

}
