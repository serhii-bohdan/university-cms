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
import java.time.ZonedDateTime;

/**
 * Data Transfer Object (DTO) representing an administrator user in the application.
 *
 * <p>This class encapsulates the essential information about an admin for data transfer
 * between layers of the application. It excludes sensitive details like the password hash
 * for security reasons.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class AdminDto extends AbstractDto {

    /**
     * The admin's first name. Must not be blank and cannot exceed 255 characters.
     */
    @NotBlank(message = "First name is mandatory")
    @Size(max = 255)
    private String firstName;

    /**
     * The admin's last name. Must not be blank and cannot exceed 255 characters.
     */
    @NotBlank(message = "Last name is mandatory")
    @Size(max = 255)
    private String lastName;

    /**
     * The admin's email address. Must be a valid email format.
     */
    @NotNull(message = "Email is mandatory")
    @Email(message = "Email address is not valid")
    private String email;

    /**
     * The ID of the role associated with the admin.
     */
    private Long roleId;

    /**
     * The date and time (including time zone) when the admin record was created.
     */
    private ZonedDateTime createdAt;

    /**
     * The date and time (including time zone) when the admin record was last updated.
     */
    private ZonedDateTime updatedAt;

}
