package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.util.annotation.UniqueUserEmail;

/**
 * Represents the base Data Transfer Object (DTO) for creating a user.
 * <p>
 * This abstract class serves as a foundation for user creation, containing
 * common fields and validation rules applicable to all user types. Subclasses
 * can extend this class to include additional fields or behaviors specific
 * to different user roles.
 *
 * @author Serhii Bohdan
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
     */
    @NotBlank(message = "First name is mandatory")
    @Size(max = 255, message = "First name must be 255 characters or less")
    private String firstName;

    /**
     * The user's last name.
     */
    @NotBlank(message = "Last name is mandatory")
    @Size(max = 255, message = "Last name must be 255 characters or less")
    private String lastName;

    /**
     * The user's email address.
     */
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email address is not valid")
    private String email;

    /**
     * The user's password.
     */
    @NotBlank(message = "Password is mandatory")
    private String password;

    /**
     * Indicates whether the user account is active.
     */
    @NotNull(message = "Status is mandatory")
    private Boolean isActive;

}
