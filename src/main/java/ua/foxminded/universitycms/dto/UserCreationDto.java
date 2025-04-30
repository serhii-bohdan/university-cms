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

/**
 * Abstract Data Transfer Object (DTO) for creating a user in the university management system.
 * <p>
 * This class serves as a base for user creation DTOs, encapsulating common attributes and validation
 * rules required for all user types, such as personal details, email, and password. It facilitates secure
 * and consistent data transfer between application layers during user creation processes. Subclasses
 * can extend this class to add role-specific fields or behaviors. The {@link UniqueUserEmail} annotation
 * ensures email uniqueness, while {@link NotBlank}, {@link Email}, and {@link Size} annotations enforce
 * data integrity.
 *
 * @author Serhii Bohdan
 * @see UniqueUserEmail
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.Email
 * @see jakarta.validation.constraints.Size
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
@UniqueUserEmail
public abstract class UserCreationDto {

    /**
     * The user's first name.
     * <p>
     * This field is mandatory and must not exceed 255 characters, as enforced by the {@link NotBlank}
     * and {@link Size} validation constraints.
     */
    @NotBlank(message = "First name is mandatory")
    @Size(max = 255, message = "First name must be 255 characters or less")
    private String firstName;

    /**
     * The user's last name.
     * <p>
     * This field is mandatory and must not exceed 255 characters, as enforced by the {@link NotBlank}
     * and {@link Size} validation constraints.
     */
    @NotBlank(message = "Last name is mandatory")
    @Size(max = 255, message = "Last name must be 255 characters or less")
    private String lastName;

    /**
     * The user's email address.
     * <p>
     * This field is mandatory and must conform to a valid email format, as enforced by the {@link NotBlank}
     * and {@link Email} constraints. The {@link UniqueUserEmail} annotation ensures it is unique across
     * all users in the system.
     */
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email address is not valid")
    private String email;

    /**
     * The user's password.
     * <p>
     * This field is mandatory, as enforced by the {@link NotBlank} constraint, and represents the initial
     * password set during user creation.
     */
    @NotBlank(message = "Password is mandatory")
    private String password;

    /**
     * The user's time zone offset from UTC, e.g., "+02:00" or "-05:00".
     * <p>
     * This field is mandatory as enforced by the {@link NotBlank} constraint.
     */
    @NotBlank(message = "Time zone offset cannot be empty")
    private String locationZoneOffset;

}
